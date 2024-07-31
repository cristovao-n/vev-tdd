package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketLotTest {

    private TicketLot ticketLot;

    private Ticket makeTicket(Long id, TicketType type, TicketStatus status) {
        return new Ticket.Builder()
                .id(id)
                .type(type)
                .status(status)
                .build();
    }

    private TicketLot makeTicketLot(Long id, List<Ticket> tickets, double discount, double normalTicketPrice) {
        return new TicketLot.Builder()
                .id(id)
                .tickets(tickets)
                .discount(discount)
                .normalTicketPrice(normalTicketPrice)
                .build();
    }

    @BeforeEach
    void init() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(this.makeTicket(0L, TicketType.VIP, TicketStatus.NOT_SOLD));
        tickets.add(this.makeTicket(0L, TicketType.HALF, TicketStatus.NOT_SOLD));
        tickets.add(this.makeTicket(0L, TicketType.NORMAL, TicketStatus.NOT_SOLD));
        this.ticketLot = this.makeTicketLot(0L, tickets, 0.25, 10);
    }

    @Test
    void testTicketPrices() {
        assertEquals(20, this.ticketLot.getVIPTicketPrice());
        assertEquals(10, this.ticketLot.getNormalTicketPrice());
        assertEquals(5, this.ticketLot.getHalfTicketPrice());
    }

    @Test
    void testDiscountGreaterThan25() {
        assertThrows(RuntimeException.class, () -> this.makeTicketLot(0L, Collections.emptyList(), 0.26, 10));
    }

    @Test
    void testDiscountEqualTo25() {
        TicketLot test = this.makeTicketLot(0L, Collections.emptyList(), 0.25, 10);
        assertEquals(0.25, test.getDiscount());
    }

    @Test
    void testDiscountLessThan25() {
        TicketLot test = this.makeTicketLot(0L, Collections.emptyList(), 0.24, 10);
        assertEquals(0.24, test.getDiscount());
    }
}
