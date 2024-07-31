package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShowTest {

    private Show show;

    private Show makeShow(LocalDate date,
                          String artist,
                          Double fee,
                          Double infrastructureExpenses,
                          List<TicketLot> ticketLots,
                          Boolean isInSpecialDate) {
        return Show.builder()
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
        this.show = this.makeShow(
                LocalDate.now(),
                "artist",
                0,
                0,
                new List<TicketLot>(),
                true
        );
    }

    @Test
    void testTicketRatios() {
        List<Ticket> allTickets = this.show.ticketLots.stream().map(ticketLot -> ticketLot.getTickets());
        int countVIP = 0;
        int countHalf = 0;
        int countNormal = 0;
        int ticketTotal = allTickets.size();
        for (Ticket ticket : allTickets) {
            switch (ticket.getType()) {
                case TicketTypes.VIP:
                    countVIP++;
                case TicketTypes.HALF:
                    countHalf++;
                case TicketTypes.NORMAL:
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
