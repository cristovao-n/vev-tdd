package ufcg.vev;

import ufcg.vev.entities.Conta;
import ufcg.vev.entities.Fatura;
import ufcg.vev.enums.TipoPagamento;

import java.math.BigDecimal;
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

    public void pagar(String codigoConta, TipoPagamento tipoPagamento) {
        Optional<Conta> contaOptional = this.getContas().stream().filter(conta -> conta.getCodigo().equals(codigoConta)).findFirst();


        Conta conta = contaOptional.get();

        if (tipoPagamento.equals(TipoPagamento.BOLETO)) {
            if (conta.getValor().compareTo(BigDecimal.valueOf(0.01)) < 0) {
                throw new RuntimeException("Valor da conta deve ser maior que 0.01 para ser paga com boleto");
            }

            if (conta.getValor().compareTo(BigDecimal.valueOf(5000)) > 0) {
                throw new RuntimeException("Valor da conta deve ser menor que 5000 para ser paga com boleto");
            }

        }

    }

}