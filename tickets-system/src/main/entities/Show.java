package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Show {
    private LocalDate date;
    private String artist;
    private Double fee;
    private Double infrastructureExpenses;
    private List<TicketLot> ticketLots;
    private Boolean isInSpecialDate;

    private Show(Builder builder) {
        this.date = builder.date;
        this.artist = builder.artist;
        this.fee = builder.fee;
        this.infrastructureExpenses = builder.infrastructureExpenses;
        this.ticketLots = builder.ticketLots;
        this.isInSpecialDate = builder.isInSpecialDate;
    }

    public Double getFee() {
        return this.fee;
    }

    public Double getInfrastructureExpenses() {
        return this.infrastructureExpenses;
    }

    public List<TicketLot> getTicketLots() {
        return new ArrayList<>(this.ticketLots);
    }

    public void setTicketLots(List<TicketLot> ticketLots) {
        this.ticketLots = ticketLots;
    }

    public void setSpecialDate(boolean isInSpecialDate) {
        this.isInSpecialDate = isInSpecialDate;
    }

    public Report generateReport() {
        int vipTicketsSold = 0;
        int halfTicketsSold = 0;
        int normalTicketsSold = 0;
        double ticketsTotalPrice = 0;

        for (TicketLot ticketLot : ticketLots) {
            for (Ticket ticket : ticketLot.getTickets()) {
                if (ticket.getStatus().equals(TicketStatus.SOLD)) {
                    switch (ticket.getType()) {
                        case VIP -> {
                            ticketsTotalPrice += ticketLot.getVIPTicketPrice();
                            vipTicketsSold++;
                        }
                        case HALF -> {
                            ticketsTotalPrice += ticketLot.getHalfTicketPrice();
                            halfTicketsSold++;
                        }
                        case NORMAL -> {
                            ticketsTotalPrice += ticketLot.getNormalTicketPrice();
                            normalTicketsSold++;
                        }
                    }
                }
            }
        }

        double EXTRA_INFRASCTRUCTURE_PERCENTAGE = 0.15;
        double infrastructureExpenses = isInSpecialDate ?
                getInfrastructureExpenses() * (1 + EXTRA_INFRASCTRUCTURE_PERCENTAGE) :
                getInfrastructureExpenses();

        double showIncome = ticketsTotalPrice -
                infrastructureExpenses -
                getFee();

        ShowStatus status = null;
        if (showIncome > 0) {
            status = ShowStatus.PROFIT;
        } else if (showIncome == 0) {
            status = ShowStatus.STABLE;
        } else {
            status = ShowStatus.LOSS;
        }
        return new Report(
                vipTicketsSold,
                halfTicketsSold,
                normalTicketsSold,
                showIncome,
                status
        );
    }

    public class Report {
        private int vipTicketsSold;
        private int halfTicketsSold;
        private int normalTicketsSold;
        private double showIncome;
        private ShowStatus showStatus;

        public Report(int vipTicketsSold,
                      int halfTicketsSold,
                      int normalTicketsSold,
                      double showIncome,
                      ShowStatus showStatus
        ) {
            this.vipTicketsSold = vipTicketsSold;
            this.halfTicketsSold = halfTicketsSold;
            this.normalTicketsSold = normalTicketsSold;
            this.showIncome = showIncome;
            this.showStatus = showStatus;
        }

        public int getVipTicketsSold() {
            return this.vipTicketsSold;
        }

        public int getHalfTicketsSold() {
            return this.halfTicketsSold;
        }

        public int getNormalTicketsSold() {
            return this.normalTicketsSold;
        }

        public double getShowIncome() {
            return this.showIncome;
        }

        public ShowStatus getShowStatus() {
            return this.showStatus;
        }
    }

    public static class Builder {
        private LocalDate date;
        private String artist;
        private Double fee;
        private Double infrastructureExpenses;
        private List<TicketLot> ticketLots;
        private Boolean isInSpecialDate;

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder artist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder fee(double fee) {
            this.fee = fee;
            return this;
        }

        public Builder infrastructureExpenses(double infrastructureExpenses) {
            this.infrastructureExpenses = infrastructureExpenses;
            return this;
        }

        public Builder ticketLots(List<TicketLot> ticketLots) {
            this.ticketLots = ticketLots;
            return this;
        }

        public Builder isInSpecialDate(boolean isInSpecialDate) {
            this.isInSpecialDate = isInSpecialDate;
            return this;
        }

        public Show build() {
            return new Show(this);
        }
    }
}
