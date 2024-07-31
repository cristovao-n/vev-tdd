package ufcg.vev.entities;

import ufcg.vev.enums.TipoPagamento;

import java.math.BigDecimal;
import java.util.Date;

public class Conta {
    private String codigo;
    private Date data;
    private BigDecimal valor;

    private TipoPagamento tipoPagamento;

    public Conta(String codigo, Date data, BigDecimal valor) {
        this.codigo = codigo;
        this.data = data;
        this.valor = valor;
    }

    public void pagarConta(String codigoConta, TipoPagamento tipoPagamento) {

    }

}
