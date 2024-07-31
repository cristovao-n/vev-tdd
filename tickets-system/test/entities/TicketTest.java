package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

    private Ticket ticket;

    private Ticket makeTicket(Long id, TicketType type) {
        return new Ticket.Builder()
                .id(id)
                .type(type)
                .build();
    }

    @BeforeEach
    void init() {
        this.ticket = this.makeTicket(0L, TicketType.NORMAL);
    }

    @Test
    void testSellTicket() {
        assertEquals(TicketStatus.NOT_SOLD, this.ticket.getStatus());
        this.ticket.sell();
        assertEquals(TicketStatus.SOLD, this.ticket.getStatus());
    }
}
