package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowTest {

    private Show show;
    private TicketLot ticketLot;

    private Ticket makeTicket(TicketType type) {
        return new Ticket.Builder()
                .type(type)
                .build();
    }

    private TicketLot makeEmptyTicketLot(Long id, Double discount, Double normalTicketPrice) {
        return new TicketLot.Builder()
                .id(id)
                .tickets(Collections.emptyList())
                .discount(discount)
                .normalTicketPrice(normalTicketPrice)
                .build();
    }

    private TicketLot makeProportionalTicketLots(Long id, Double discount, Double normalTicketPrice) {
        List<Ticket> tenTickets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tenTickets.add(makeTicket(TicketType.VIP));
        }
        tenTickets.add(makeTicket(TicketType.HALF));
        for (int i = 0; i < 6; i++) {
            tenTickets.add(makeTicket(TicketType.NORMAL));
        }
        return new TicketLot
                .Builder()
                .id(id)
                .tickets(tenTickets)
                .discount(discount)
                .normalTicketPrice(normalTicketPrice)
                .build();
    }

    private Show makeShow(LocalDate date,
                          String artist,
                          Double fee,
                          Double infrastructureExpenses,
                          List<TicketLot> ticketLots,
                          Boolean isInSpecialDate) {
        return new Show.Builder()
                .date(date)
                .artist(artist)
                .fee(fee)
                .infrastructureExpenses(infrastructureExpenses)
                .ticketLots(ticketLots)
                .isInSpecialDate(isInSpecialDate)
                .build();
    }

    @BeforeEach
    void init() {
        this.ticketLot = this.makeEmptyTicketLot(0L, 0.25, 10D);
        this.show = this.makeShow(
                LocalDate.now(),
                "artist",
                0.0,
                0.0,
                new ArrayList<>(Collections.singletonList(this.ticketLot)),
                true
        );
    }

    @Test
    void testTicketRatios() {
        this.show.setTicketLots(Collections.singletonList(this.makeProportionalTicketLots(0L, 0.25, 10D)));
        List<Ticket> allTickets = this.show.getTicketLots().stream().flatMap(ticketLot -> ticketLot.getTickets().stream()).toList();
        int countVIP = 0;
        int countHalf = 0;
        int countNormal = 0;
        int ticketTotal = allTickets.size();
        for (Ticket ticket : allTickets) {
            switch (ticket.getType()) {
                case VIP -> countVIP++;
                case HALF -> countHalf++;
                case NORMAL -> countNormal++;
            }
        }
        double vipRatio = (double) countVIP / ticketTotal;
        double halfRatio = (double) countHalf / ticketTotal;
        double normalRatio = (double) countNormal / ticketTotal;

        assertTrue(vipRatio >= 0.2 && vipRatio <= 0.3);
        assertTrue(halfRatio <= 0.1);
        double expectedNormalRatio = 1 - (vipRatio + halfRatio);
        assertEquals(expectedNormalRatio, normalRatio);
    }

    @Test
    void testUnproportionalTicketRatios() {
        this.show.setTicketLots(Collections.singletonList(this.makeProportionalTicketLots(0L, 0.25, 10D)));
        List<Ticket> allTickets = new ArrayList<>(this.show.getTicketLots().stream().flatMap(ticketLot -> ticketLot.getTickets().stream()).toList());
        allTickets.remove(0);
        allTickets.remove(1);

        int countVIP = 0;
        int countHalf = 0;
        int countNormal = 0;
        int ticketTotal = allTickets.size();
        for (Ticket ticket : allTickets) {
            switch (ticket.getType()) {
                case VIP -> countVIP++;
                case HALF -> countHalf++;
                case NORMAL -> countNormal++;
            }
        }
        double vipRatio = (double) countVIP / ticketTotal;
        double halfRatio = (double) countHalf / ticketTotal;
        double normalRatio = (double) countNormal / ticketTotal;

        assertFalse(vipRatio >= 0.2 && vipRatio <= 0.3);
        assertFalse(halfRatio <= 0.1);
        double expectedNormalRatio = 1 - (vipRatio + halfRatio);
        assertEquals(expectedNormalRatio, normalRatio);
    }
}
