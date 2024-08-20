package ufcg.vev.functionalTests;

import org.junit.jupiter.api.Test;
import ufcg.vev.ProcessadorContas;
import ufcg.vev.entities.Conta;
import ufcg.vev.entities.Fatura;
import ufcg.vev.enums.TipoPagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessadorContasAVLTest {

    private Fatura criarFaturaComValor(BigDecimal valorConta) {
        return new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Teste",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 6), valorConta)
                )));
    }

    @Test
    void testBoletoRejeitadoPorValorMinimo() {
        Fatura fatura = criarFaturaComValor(BigDecimal.valueOf(0.0001));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("Valor da conta deve ser maior que 0.01 para ser paga com boleto", e.getMessage());
    }

    @Test
    void testBoletoAceitoNoLimiteMinimo() {
        Fatura fatura = criarFaturaComValor(BigDecimal.valueOf(0.01));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        assertDoesNotThrow(() -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
    }

    @Test
    void testBoletoAceitoAcimaDoLimiteMinimo() {
        Fatura fatura = criarFaturaComValor(BigDecimal.valueOf(0.011));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        assertDoesNotThrow(() -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
    }

    @Test
    void testBoletoAceitoValorMedio() {
        Fatura fatura = criarFaturaComValor(BigDecimal.valueOf(300));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        assertDoesNotThrow(() -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
    }

    @Test
    void testBoletoAceitoAbaixoDoLimiteMaximo() {
        Fatura fatura = criarFaturaComValor(BigDecimal.valueOf(4999.999));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        assertDoesNotThrow(() -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
    }

    @Test
    void testBoletoAceitoNoLimiteMaximo() {
        Fatura fatura = criarFaturaComValor(BigDecimal.valueOf(5000));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        assertDoesNotThrow(() -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
    }

    @Test
    void testBoletoRejeitadoPorValorMaximoExcedido() {
        Fatura fatura = criarFaturaComValor(BigDecimal.valueOf(5000.01));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("Valor da conta deve ser menor que 5000 para ser paga com boleto", e.getMessage());
    }

    @Test
    void testCartaoCredito20Dias() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 1), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 1));
        assertEquals(BigDecimal.valueOf(100), processadorContas.getValorPago());
    }

    @Test
    void testCartaoCredito16Dias() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 4), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 5));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    void testCartaoCredito15Dias() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 6));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    void testCartaoCredito8Dias() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 12), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 12));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    void testBoletoTransferencia10Dias() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 10));
        assertEquals(BigDecimal.valueOf(100), processadorContas.getValorPago());
    }

    @Test
    void testBoletoTransferencia1Dia() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));
        assertEquals(BigDecimal.valueOf(100), processadorContas.getValorPago());
    }

    @Test
    void testBoletoTransferenciaDiaAtual() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 20));
        assertEquals(BigDecimal.valueOf(100), processadorContas.getValorPago());
    }

    @Test
    void testBoletoTransferencia1DiaDepois() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 21), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 21));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    void testBoletoTransferencia20DiasDepois() {
        Fatura fatura = new Fatura(
                new Date(2023 - 1900, Calendar.FEBRUARY, 20),
                "Cliente",
                new ArrayList<>(List.of(
                        new Conta("1", new Date(2023 - 1900, Calendar.MARCH, 12), BigDecimal.valueOf(100))
                )));
        ProcessadorContas processadorContas = new ProcessadorContas(fatura);
        processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.MARCH, 12));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }


}
