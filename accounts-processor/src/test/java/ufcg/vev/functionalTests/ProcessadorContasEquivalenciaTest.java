package ufcg.vev.functionalTests;

import org.junit.jupiter.api.Test;
import ufcg.vev.ProcessadorContas;
import ufcg.vev.entities.Conta;
import ufcg.vev.entities.Fatura;
import ufcg.vev.enums.FaturaStatus;
import ufcg.vev.enums.TipoPagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessadorContasEquivalenciaTest {

    private final Fatura faturaBoleto = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Cristovão",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 15), BigDecimal.valueOf(200))
            )));

    private final Fatura faturaCartao = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Ismael",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(100)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(200))
            )));

    private final Fatura faturaSomaPagamentosPaga = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Suelen",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(400)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(600))
            )));

    private final Fatura faturaSomaPagamentosPendente = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "John",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(300)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(400))
            )));

    @Test
    void testBoletoPagoAposDataConta() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));
        assertEquals(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(1.10)), processadorContas.getValorPago());
    }

    @Test
    void testBoletoPagoAntesDataConta() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        processadorContas.pagarConta("2", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 14));
        assertEquals(BigDecimal.valueOf(200), processadorContas.getValorPago());
    }

    @Test
    void testSomaPagamentosMaiorOuIgualValorFatura() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaSomaPagamentosPaga);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));
        processadorContas.pagarConta("2", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));

        assertEquals(FaturaStatus.PAGA, processadorContas.getStatusFatura());
    }

    @Test
    void testSomaPagamentosMenorValorFatura() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaSomaPagamentosPendente);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 20));
        processadorContas.pagarConta("2", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 20));

        assertEquals(FaturaStatus.PENDENTE, processadorContas.getStatusFatura());
    }

    @Test
    void testPagamentoCartaoAntes15Dias() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta("1", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 4));
        assertEquals(BigDecimal.valueOf(100), processadorContas.getValorPago());
    }

    @Test
    void testPagamentoCartaoAte15DiasAntes() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta("2", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 10));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    void testPagamentoCartaoAposDataFatura() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta("1", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 21));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    void testPagamentoBoletoAntesOuNoDiaDataFatura() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        processadorContas.pagarConta("2", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 15));
        assertEquals(BigDecimal.valueOf(200), processadorContas.getValorPago());
    }

    @Test
    void testPagamentoBoletoAposDataFatura() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        processadorContas.pagarConta("2", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 21));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }
}
