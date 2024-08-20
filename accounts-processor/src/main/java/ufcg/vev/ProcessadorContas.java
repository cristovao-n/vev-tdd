package ufcg.vev;

import ufcg.vev.entities.Conta;
import ufcg.vev.entities.Fatura;
import ufcg.vev.enums.FaturaStatus;
import ufcg.vev.enums.TipoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProcessadorContas {

    private Fatura fatura;

    public ProcessadorContas(Fatura fatura) {
        this.fatura = fatura;
    }

    public List<Conta> getContas() {
        return fatura.getContas();
    }

    public BigDecimal getValorPago() {
        return this.fatura.getValorPago();
    }

    public FaturaStatus getStatusFatura() {
        return this.fatura.getStatus();
    }

    public void pagarConta(String codigoConta, TipoPagamento tipoPagamento, Date dataPagamento) {
        Optional<Conta> contaOptional = this.getContas().stream().filter(conta -> conta.getCodigo().equals(codigoConta)).findFirst();

        if (contaOptional.isEmpty()) {
            throw new RuntimeException("Codigo de conta invalido ou conta com esse codigo inexistente");
        }
        Conta conta = contaOptional.get();

        if (conta.getData().after(fatura.getData())) {
            return;
        }

        if (tipoPagamento.equals(TipoPagamento.BOLETO)) {
            if (conta.getValor().compareTo(BigDecimal.valueOf(0.01)) < 0) {
                throw new RuntimeException("Valor da conta deve ser maior que 0.01 para ser paga com boleto");
            }

            if (conta.getValor().compareTo(BigDecimal.valueOf(5000)) > 0) {
                throw new RuntimeException("Valor da conta deve ser menor que 5000 para ser paga com boleto");
            }

            if (dataPagamento.before(fatura.getData())) {
                if (dataPagamento.after(conta.getData())) {
                    fatura.addValorPagamento(conta.getValor().multiply(BigDecimal.valueOf(1.1)));
                } else {
                    fatura.addValorPagamento(conta.getValor());
                }
            }

            verificaSeFaturaFoiPaga();
            return;
        }

        if (tipoPagamento.equals(TipoPagamento.CARTAO_CREDITO)) {
            LocalDateTime ldt = LocalDateTime.ofInstant(fatura.getData().toInstant(), ZoneId.systemDefault());
            LocalDateTime minusDays = ldt.minusDays(15);
            Date faturaMenos15Dias = Date.from(minusDays.atZone(ZoneId.systemDefault()).toInstant());

            if (dataPagamento.before(faturaMenos15Dias)) {
                fatura.addValorPagamento(conta.getValor());
                verificaSeFaturaFoiPaga();
            }
            return;
        }
        fatura.addValorPagamento(conta.getValor());
        verificaSeFaturaFoiPaga();

    }

    private void verificaSeFaturaFoiPaga() {
        if (this.fatura.getValorPago().compareTo(this.fatura.getValor()) >= 0) {
            this.fatura.pagarFatura();
        }
    }

}