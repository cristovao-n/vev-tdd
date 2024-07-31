package entities;

public class Ticket {

    private Long id;
    private TicketType type;
    private TicketStatus status;

    private Ticket(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.status = builder.status;
    }

    public TicketType getType() {
        return this.type;
    }

    public static class Builder {
        private Long id;
        private TicketType type;
        private TicketStatus status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder type(TicketType type) {
            this.type = type;
            return this;
        }

        public Builder status(TicketStatus status) {
            this.status = status;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }
}
