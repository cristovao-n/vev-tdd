package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShowTest {

    private Show show;
    private Ticket ticket;
    private TicketLot ticketLot;

    private Ticket makeTicket(TicketType type) {
        return new Ticket.Builder()
                .type(type)
                .build();
    }

    private TicketLot makeEmptyTicketLot(List<Ticket> tickets) {
        return new TicketLot
                .Builder()
                .tickets(tickets)
                .build();
    }

    private TicketLot makeProportionalTicketLots() {
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
                .tickets(tenTickets)
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
        this.ticketLot = this.makeEmptyTicketLot(Collections.emptyList());
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
        this.show.setTicketLots(Collections.singletonList(this.makeProportionalTicketLots()));
        List<Ticket> allTickets = this.show.getTicketLots().stream().flatMap(ticketLot -> ticketLot.getTickets().stream()).toList();
        int countVIP = 0;
        int countHalf = 0;
        int countNormal = 0;
        int ticketTotal = allTickets.size();
        for (Ticket ticket : allTickets) {
            switch (ticket.getType()) {
                case VIP:
                    countVIP++;
                case HALF:
                    countHalf++;
                case NORMAL:
                    countNormal++;
            }
        }
        double vipRatio = countVIP / ticketTotal;
        double halfRatio = countHalf / ticketTotal;
        double normalRatio = countNormal / ticketTotal;

        assertTrue(vipRatio >= 0.2 && vipRatio <= 0.3);
        assertTrue(halfRatio <= 0.1);
        assertEquals((vipRatio + halfRatio - 1), normalRatio);
    }
}
