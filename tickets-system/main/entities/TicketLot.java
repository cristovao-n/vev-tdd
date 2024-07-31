package entities;

import java.util.ArrayList;
import java.util.List;

public class TicketLot {
    private List<Ticket> tickets;

    private TicketLot(Builder builder) {
        this.tickets = builder.tickets;
    }

    public List<Ticket> getTickets() {
        return new ArrayList<>(this.tickets);
    }

    public static class Builder {

        private List<Ticket> tickets;

        public Builder tickets(List<Ticket> tickets) {
            this.tickets = tickets;
            return this;
        }

        public TicketLot build() {
            return new TicketLot(this);
        }
    }
}
