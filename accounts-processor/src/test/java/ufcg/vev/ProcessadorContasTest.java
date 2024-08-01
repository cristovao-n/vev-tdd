package ufcg.vev;

import org.junit.jupiter.api.Test;
import ufcg.vev.entities.Conta;
import ufcg.vev.entities.Fatura;
import ufcg.vev.enums.TipoPagamento;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProcessadorContasTest {

    private final Fatura fatura1 = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Cristovao",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(500)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(400)),
                    new Conta("3", new Date(2023 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(600))
            )));
    private final Fatura fatura2 = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Ismael",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(700)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 17), BigDecimal.valueOf(800))
            )));
    private final Fatura fatura3 = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Suelen",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(700)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 17), BigDecimal.valueOf(800))
            )));

    private final Fatura faturaBoleto = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(0.001)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(5001)),
                    new Conta("3", new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(100))
            )));

    private final Fatura faturaCartao = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023 - 1900, Calendar.FEBRUARY, 4), BigDecimal.valueOf(100)),
                    new Conta("2", new Date(2023 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100))
            )));

    @Test
    void testContaInexistente() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta("999999", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("Codigo de conta invalido ou conta com esse codigo inexistente", e.getMessage());
    }

    @Test
    void testBoletoMenorQue1Centavo() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta("1", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("Valor da conta deve ser maior que 0.01 para ser paga com boleto", e.getMessage());
    }

    @Test
    void testBoletoMaiorQue5000() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta("2", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("Valor da conta deve ser menor que 5000 para ser paga com boleto", e.getMessage());
    }

    @Test
    void testBoletoAtrasado() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        processadorContas.pagarConta("3", TipoPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 21));
        assertEquals(processadorContas.getValorPago(), BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(1.1)));

    }

    @Test
    void testPagamentoCartaoConsiderado() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta("1", TipoPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 4));
        assertEquals(processadorContas.getValorPago(), BigDecimal.valueOf(100));
    }


}