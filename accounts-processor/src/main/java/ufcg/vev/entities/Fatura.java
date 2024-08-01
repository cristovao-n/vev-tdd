package ufcg.vev.entities;

import ufcg.vev.enums.FaturaStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Fatura {
    private Date data;
    private BigDecimal valorPago = BigDecimal.ZERO;
    private String nomeCliente;

    private List<Conta> contas;
    private BigDecimal valor = BigDecimal.ZERO;

    private FaturaStatus status = FaturaStatus.PENDENTE;

    public Fatura(Date data, String nomeCliente, List<Conta> contas) {
        this.data = data;
        this.nomeCliente = nomeCliente;
        this.contas = contas;
        this.valor = contas.stream().map(Conta::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Conta> getContas() {
        return this.contas;
    }

    public void addValorPagamento(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Valor nao pode ser negativo");
        }
        this.valorPago = this.valorPago.add(valor);
    }

    public BigDecimal getValorPago() {
        return this.valorPago;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Date getData() {
        return data;
    }

    public void pagarFatura() {
        this.status = FaturaStatus.PAGA;
    }

    public FaturaStatus getStatus() {
        return this.status;
    }

}
