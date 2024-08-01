package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static entities.EntityMaker.*;
import static org.junit.jupiter.api.Assertions.*;

public class TicketLotTest {

    private TicketLot ticketLot;

    @BeforeEach
    void init() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(makeTicket(0L, TicketType.VIP));
        tickets.add(makeTicket(0L, TicketType.HALF));
        tickets.add(makeTicket(0L, TicketType.NORMAL));
        this.ticketLot = makeTicketLot(0L, tickets, 0D, 10D);
    }

    @Test
    void testTicketPrices() {
        assertEquals(20, this.ticketLot.getVIPTicketPrice());
        assertEquals(10, this.ticketLot.getNormalTicketPrice());
        assertEquals(5, this.ticketLot.getHalfTicketPrice());
    }

    @Test
    void testDiscountGreaterThan25() {
        assertThrows(RuntimeException.class, () -> makeTicketLot(0L, Collections.emptyList(), 0.26, 10D));
    }

    @Test
    void testDiscountEqualTo25() {
        TicketLot test = makeTicketLot(0L, Collections.emptyList(), 0.25, 10D);
        assertEquals(0.25, test.getDiscount());
    }

    @Test
    void testDiscountLessThan25() {
        TicketLot test = makeTicketLot(0L, Collections.emptyList(), 0.24, 10D);
        assertEquals(0.24, test.getDiscount());
    }

    @Test
    void testDiscountIsApplied() {
        TicketLot test = makeTicketLot(0L, Collections.emptyList(), 0.25, 10D);
        assertEquals(15, test.getVIPTicketPrice());
        assertEquals(7.5, test.getNormalTicketPrice());
        assertEquals(5, test.getHalfTicketPrice());
    }
}
