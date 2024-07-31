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

    public List<TicketLot> getTicketLots() {
        return new ArrayList<>(this.ticketLots);
    }

    public void setTicketLots(List<TicketLot> ticketLots) {
        this.ticketLots = ticketLots;
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
