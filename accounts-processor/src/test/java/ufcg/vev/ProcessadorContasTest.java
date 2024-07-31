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
            new Date(2023, Calendar.FEBRUARY, 20),
            "Cristovao",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023, Calendar.FEBRUARY, 20), BigDecimal.valueOf(500)),
                    new Conta("2", new Date(2023, Calendar.FEBRUARY, 20), BigDecimal.valueOf(400)),
                    new Conta("3", new Date(2023, Calendar.FEBRUARY, 20), BigDecimal.valueOf(600))
            )));
    private final Fatura fatura2 = new Fatura(
            new Date(2023, Calendar.FEBRUARY, 20),
            "Ismael",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023, Calendar.FEBRUARY, 5), BigDecimal.valueOf(700)),
                    new Conta("2", new Date(2023, Calendar.FEBRUARY, 17), BigDecimal.valueOf(800))
            )));
    private final Fatura fatura3 = new Fatura(
            new Date(2023, Calendar.FEBRUARY, 20),
            "Suelen",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023, Calendar.FEBRUARY, 6), BigDecimal.valueOf(700)),
                    new Conta("2", new Date(2023, Calendar.FEBRUARY, 17), BigDecimal.valueOf(800))
            )));

    private final Fatura faturaContasInvalidasBoleto = new Fatura(
            new Date(2023, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta("1", new Date(2023, Calendar.FEBRUARY, 6), BigDecimal.valueOf(0.001)),
                    new Conta("2", new Date(2023, Calendar.FEBRUARY, 17), BigDecimal.valueOf(5001))
            )));

    @Test
    void testBoletoMenorQue1Centavo() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaContasInvalidasBoleto);
        assertThrows(RuntimeException.class, () -> processadorContas.pagar("0", TipoPagamento.BOLETO));
        assertThrows(RuntimeException.class, () -> processadorContas.pagar("1", TipoPagamento.BOLETO));
    }
}