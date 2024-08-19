package functionalTests;

import entities.Show;
import entities.Ticket;
import entities.TicketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static entities.EntityMaker.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShowBoundaryTest {

    private Show show;

    @BeforeEach
    void init() {
        this.show = makeShow(
                LocalDate.now(),
                "artist",
                0.0,
                0.0,
                Collections.singletonList(makeTicketLot(0L, Collections.emptyList(), 0.25, 10D)),
                true
        );
    }

    private List<Ticket> createBoundaryTickets(int total, int vip, int half) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < vip; i++) {
            tickets.add(makeTicket((long) i, TicketType.VIP));
        }
        for (int i = 0; i < half; i++) {
            tickets.add(makeTicket((long) i + vip, TicketType.HALF));
        }
        for (int i = 0; i < (total - vip - half); i++) {
            tickets.add(makeTicket((long) i + vip + half, TicketType.NORMAL));
        }
        return tickets;
    }

    @Test
    void testBoundaryVIPQuantity() {
        List<Ticket> tickets = createBoundaryTickets(100, 20, 10);
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, tickets, 0.25, 10D)));
        assertEquals(20, tickets.stream().filter(t -> t.getType() == TicketType.VIP).count());
    }

    @Test
    void testBoundaryVIPQuantityAbove() {
        List<Ticket> tickets = createBoundaryTickets(100, 21, 10);
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, tickets, 0.25, 10D)));
        assertEquals(21, tickets.stream().filter(t -> t.getType() == TicketType.VIP).count());
    }

    @Test
    void testBoundaryVIPQuantityBelow() {
        List<Ticket> tickets = createBoundaryTickets(100, 19, 10);
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, tickets, 0.25, 10D)));
        assertEquals(19, tickets.stream().filter(t -> t.getType() == TicketType.VIP).count());
    }

    @Test
    void testBoundaryHalfQuantity() {
        List<Ticket> tickets = createBoundaryTickets(100, 30, 10);
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, tickets, 0.25, 10D)));
        assertEquals(10, tickets.stream().filter(t -> t.getType() == TicketType.HALF).count());
    }

    @Test
    void testBoundaryHalfQuantityAbove() {
        List<Ticket> tickets = createBoundaryTickets(100, 30, 11);
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, tickets, 0.25, 10D)));
        assertEquals(11, tickets.stream().filter(t -> t.getType() == TicketType.HALF).count());
    }

    @Test
    void testBoundaryHalfQuantityBelow() {
        List<Ticket> tickets = createBoundaryTickets(100, 30, 9);
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, tickets, 0.25, 10D)));
        assertEquals(9, tickets.stream().filter(t -> t.getType() == TicketType.HALF).count());
    }
}

