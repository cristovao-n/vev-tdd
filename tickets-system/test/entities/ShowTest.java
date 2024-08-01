package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Show.Report;

import static entities.EntityMaker.*;
import static org.junit.jupiter.api.Assertions.*;

class ShowTest {

    private Show show;
    private TicketLot ticketLot;

    private TicketLot makeEmptyTicketLot(Long id, Double discount, Double normalTicketPrice) {
        return makeTicketLot(id, Collections.emptyList(), discount, normalTicketPrice);
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

    private TicketLot makeProportionalTicketLots(Long id, Double discount, Double normalTicketPrice) {
        List<Ticket> tenTickets = this.makeProportionalTicketList(10);
        return makeTicketLot(id, tenTickets, discount, normalTicketPrice);
    }

    @BeforeEach
    void init() {
        this.ticketLot = this.makeEmptyTicketLot(0L, 0.25, 10D);
        this.show = makeShow(
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
        this.show.setTicketLots(Collections.singletonList(this.makeProportionalTicketLots(0L, 0.25, 10D)));
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
        this.show.setTicketLots(Collections.singletonList(this.makeProportionalTicketLots(0L, 0.25, 10D)));
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
        List<Ticket> ticketList = this.makeProportionalTicketList(100);
        ticketList.forEach(Ticket::sell);
        TicketLot newTicketLot = makeTicketLot(0L, ticketList, 0.25, 10D);
        Show newShow = makeShow(
                LocalDate.now(),
                "artist",
                0.0,
                0.0,
                Collections.singletonList(newTicketLot),
                true
        );

        int VIPTicketsSold = 30;
        int halfTicketsSold = 10;
        int normalTicketsSold = 60;
        double EXTRA_INFRASCTRUCTURE_PERCENTAGE = 0.15;
        double showIncome = VIPTicketsSold * ticketLot.getVIPTicketPrice() +
                            halfTicketsSold * ticketLot.getHalfTicketPrice() +
                            normalTicketsSold * ticketLot.getNormalTicketPrice() -
                            newShow.getInfrastructureExpenses() * (1 + EXTRA_INFRASCTRUCTURE_PERCENTAGE) -
                            newShow.getFee();
        ShowStatus status = null;
        if (showIncome > 0) {
            status = ShowStatus.PROFIT;
        } else if (showIncome == 0) {
            status = ShowStatus.STABLE;
        } else {
            status = ShowStatus.LOSS;
        }

        Report report = newShow.generateReport();
        assertEquals(VIPTicketsSold, report.getVipTicketsSold());
        assertEquals(halfTicketsSold, report.getHalfTicketsSold());
        assertEquals(normalTicketsSold, report.getNormalTicketsSold());
        assertEquals(showIncome, report.getShowIncome());
        assertEquals(status, report.getShowStatus());
    }

    @Test
    void testNormalDateReportCreation() {
        List<Ticket> ticketList = this.makeProportionalTicketList(100);
        ticketList.forEach(Ticket::sell);
        TicketLot newTicketLot = makeTicketLot(0L, ticketList, 0.25, 10D);
        Show newShow = makeShow(
                LocalDate.now(),
                "artist",
                0.0,
                0.0,
                Collections.singletonList(newTicketLot),
                false
        );

        int VIPTicketsSold = 30;
        int halfTicketsSold = 10;
        int normalTicketsSold = 60;
        double showIncome = VIPTicketsSold * ticketLot.getVIPTicketPrice() +
                halfTicketsSold * ticketLot.getHalfTicketPrice() +
                normalTicketsSold * ticketLot.getNormalTicketPrice() -
                newShow.getInfrastructureExpenses() -
                newShow.getFee();
        ShowStatus status = null;
        if (showIncome > 0) {
            status = ShowStatus.PROFIT;
        } else if (showIncome == 0) {
            status = ShowStatus.STABLE;
        } else if (showIncome < 0) {
            status = ShowStatus.LOSS;
        }

        Report report = newShow.generateReport();
        assertEquals(VIPTicketsSold, report.getVipTicketsSold());
        assertEquals(halfTicketsSold, report.getHalfTicketsSold());
        assertEquals(normalTicketsSold, report.getNormalTicketsSold());
        assertEquals(showIncome, report.getShowIncome());
        assertEquals(status, report.getShowStatus());
    }
}
