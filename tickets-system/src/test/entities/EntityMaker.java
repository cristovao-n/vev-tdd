package entities;

import java.time.LocalDate;
import java.util.List;

public class EntityMaker {

    public static Ticket makeTicket(Long id, TicketType type) {
        return new Ticket.Builder()
                .id(id)
                .type(type)
                .build();
    }

    public static TicketLot makeTicketLot(Long id, List<Ticket> tickets, Double discount, Double normalTicketPrice) {
        return new TicketLot.Builder()
                .id(id)
                .tickets(tickets)
                .discount(discount)
                .normalTicketPrice(normalTicketPrice)
                .build();
    }

    public static Show makeShow(LocalDate date,
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
}
