package entities;

public class Ticket {

    private TicketType type;

    private Ticket(Builder builder) {
        this.type = builder.type;
    }
    public TicketType getType() {
        return this.type;
    }

    public static class Builder {
        private TicketType type;

        public Builder type(TicketType type) {
            this.type = type;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }
}
