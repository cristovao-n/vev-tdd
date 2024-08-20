package functionalTests;

import entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static entities.EntityMaker.makeTicket;
import static entities.EntityMaker.makeTicketLot;
import static entities.EntityMaker.makeShow;
import static org.junit.jupiter.api.Assertions.*;

public class ShowDecisionTableTest {

    private Show show;

    @BeforeEach
    void init() {
        this.show = makeShow(
                LocalDate.now(),
                "artist",
                1000.0,
                2000.0,
                Collections.singletonList(makeTicketLot(0L, Collections.emptyList(), 0.0, 10D)),
                true
        );
    }

    private TicketLot makeProportionalTicketLots(Long id, Double discount, Double normalTicketPrice) {
        List<Ticket> tenTickets = this.makeProportionalTicketList(10);
        return makeTicketLot(id, tenTickets, discount, normalTicketPrice);
    }

    private List<Ticket> makeProportionalTicketList(int quantity) {
        List<Ticket> tickets = new ArrayList<>();
        int VIPQuantity = (int) (quantity * 0.3);
        int halfQuantity = (int) (quantity * 0.1);
        int normalQuantity = quantity - (VIPQuantity + halfQuantity);

        for (int i = 0; i < VIPQuantity; i++) {
            tickets.add(makeTicket((long) i, TicketType.VIP));
        }
        for (int i = 0; i < halfQuantity; i++) {
            tickets.add(makeTicket((long) i + VIPQuantity, TicketType.HALF));
        }
        for (int i = 0; i < normalQuantity; i++) {
            tickets.add(makeTicket((long) i + VIPQuantity + halfQuantity, TicketType.NORMAL));
        }
        return tickets;
    }

    @Test
    void testTicketRatios() {
        this.show.setTicketLots(Collections.singletonList(makeProportionalTicketLots(0L, 0.25, 10D)));
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
        assertEquals(0.1, halfRatio);
        double expectedNormalRatio = 1 - (vipRatio + halfRatio);
        assertEquals(expectedNormalRatio, normalRatio);
    }

    @Test
    void testUnproportionalTicketRatios() {
        this.show.setTicketLots(Collections.singletonList(makeProportionalTicketLots(0L, 0.25, 10D)));
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
        assertNotEquals(0.1, halfRatio);
        double expectedNormalRatio = 1 - (vipRatio + halfRatio);
        assertEquals(expectedNormalRatio, normalRatio);
    }

    @Test
    void testSpecialDateReportCreation() {
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, createTickets(30, 10, 60), 0.25, 10D)));
        this.show.getTicketLots().forEach(ticketLot -> ticketLot.getTickets().forEach(Ticket::sell));
        Show.Report report = this.show.generateReport();
        double expectedIncome = calculateExpectedIncome(0.25, true);
        assertEquals(30, report.getVipTicketsSold());
        assertEquals(10, report.getHalfTicketsSold());
        assertEquals(60, report.getNormalTicketsSold());
        assertEquals(expectedIncome, report.getShowIncome());
        assertEquals(ShowStatus.LOSS, report.getShowStatus());
    }

    @Test
    void testNormalDateReportCreation() {
        this.show.setSpecialDate(false);
        this.show.setTicketLots(Collections.singletonList(makeTicketLot(0L, createTickets(30, 10, 60), 0.25, 10D)));
        this.show.getTicketLots().forEach(ticketLot -> ticketLot.getTickets().forEach(Ticket::sell));
        Show.Report report = this.show.generateReport();
        double expectedIncome = calculateExpectedIncome(0.25, false);
        assertEquals(30, report.getVipTicketsSold());
        assertEquals(10, report.getHalfTicketsSold());
        assertEquals(60, report.getNormalTicketsSold());
        assertEquals(expectedIncome, report.getShowIncome());
        assertEquals(ShowStatus.LOSS, report.getShowStatus());
    }

    @Test
    void testDiscountGreaterThan25() {
        assertThrows(RuntimeException.class, () -> makeTicketLot(0L, Collections.emptyList(), 0.26, 10D));
    }

    private List<Ticket> createTickets(int vip, int half, int normal) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < vip; i++) {
            tickets.add(makeTicket((long) i, TicketType.VIP));
        }
        for (int i = 0; i < half; i++) {
            tickets.add(makeTicket((long) i + vip, TicketType.HALF));
        }
        for (int i = 0; i < normal; i++) {
            tickets.add(makeTicket((long) i + vip + half, TicketType.NORMAL));
        }
        return tickets;
    }

    private double calculateExpectedIncome(double discount, boolean isInSpecialDate) {
        double ticketsTotalPrice = 0;
        for (TicketLot ticketLot : this.show.getTicketLots()) {
            for (Ticket ticket : ticketLot.getTickets()) {
                if (ticket.getStatus().equals(TicketStatus.SOLD)) {
                    switch (ticket.getType()) {
                        case VIP -> ticketsTotalPrice += ticketLot.getVIPTicketPrice();
                        case HALF -> ticketsTotalPrice += ticketLot.getHalfTicketPrice();
                        case NORMAL -> ticketsTotalPrice += ticketLot.getNormalTicketPrice();
                    }
                }
            }
        }

        double infrastructureExpenses = isInSpecialDate ?
                this.show.getInfrastructureExpenses() * 1.15 :
                this.show.getInfrastructureExpenses();

        return ticketsTotalPrice - infrastructureExpenses - this.show.getFee();
    }
}
