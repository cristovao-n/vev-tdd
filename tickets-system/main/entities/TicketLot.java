package entities;

import java.util.ArrayList;
import java.util.List;

public class TicketLot {
    private Long id;
    private List<Ticket> tickets;
    private Double discount;
    private Double normalTicketPrice;

    private TicketLot(Builder builder) {
        if (builder.discount > 0.25) throw new RuntimeException("Discount cannot be greater than 25%");
        this.id= builder.id;
        this.tickets = builder.tickets;
        this.discount = builder.discount;
        this.normalTicketPrice = builder.normalTicketPrice;
    }

    public List<Ticket> getTickets() {
        return new ArrayList<>(this.tickets);
    }

    public Double getDiscount() {
        return this.discount;
    }

    public Double getVIPTicketPrice() {
        return 2 * this.normalTicketPrice * (1 - this.discount);
    }

    public Double getNormalTicketPrice() {
        return this.normalTicketPrice * (1 - this.discount);
    }

    public Double getHalfTicketPrice() {
        return this.normalTicketPrice / 2;
    }

    public static class Builder {

        private Long id;
        private List<Ticket> tickets;
        private Double discount;
        private Double normalTicketPrice;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tickets(List<Ticket> tickets) {
            this.tickets = tickets;
            return this;
        }

        public Builder discount(Double discount) {
            this.discount = discount;
            return this;
        }

        public Builder normalTicketPrice(Double normalTicketPrice) {
            this.normalTicketPrice = normalTicketPrice;
            return this;
        }

        public TicketLot build() {
            return new TicketLot(this);
        }
    }
}
