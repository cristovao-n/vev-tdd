package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static entities.EntityMaker.makeTicket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

    private Ticket ticket;

    @BeforeEach
    void init() {
        this.ticket = makeTicket(0L, TicketType.NORMAL);
    }

    @Test
    void testSellTicket() {
        assertEquals(TicketStatus.NOT_SOLD, this.ticket.getStatus());
        this.ticket.sell();
        assertEquals(TicketStatus.SOLD, this.ticket.getStatus());
    }
}
