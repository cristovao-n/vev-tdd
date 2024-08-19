package functionalTests;

import entities.Ticket;
import entities.TicketLot;
import entities.TicketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static entities.EntityMaker.makeTicket;
import static entities.EntityMaker.makeTicketLot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketLotPartitionTest {

    private TicketLot ticketLot0;
    private TicketLot ticketLot25;

    @BeforeEach
    void init() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(makeTicket(0L, TicketType.VIP));
        tickets.add(makeTicket(0L, TicketType.HALF));
        tickets.add(makeTicket(0L, TicketType.NORMAL));
        this.ticketLot0 = makeTicketLot(0L, tickets, 0.0, 10D);
        this.ticketLot25 = makeTicketLot(0L, tickets, 0.25, 10D);
    }

    @Test
    void testVIPPriceWithDiscount() {
        assertEquals(15, this.ticketLot25.getVIPTicketPrice());
    }

    @Test
    void testVIPPriceWithoutDiscount() {
        assertEquals(20, this.ticketLot0.getVIPTicketPrice());
    }

    @Test
    void testNormalTicketPriceWithDiscount() {
        assertEquals(7.5, this.ticketLot25.getNormalTicketPrice());
    }

    @Test
    void testNormalTicketPriceWithoutDiscount() {
        assertEquals(10, this.ticketLot0.getNormalTicketPrice());
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
