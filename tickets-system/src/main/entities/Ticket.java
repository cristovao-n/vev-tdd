package entities;

public class Ticket {

    private Long id;
    private TicketType type;
    private TicketStatus status;

    private Ticket(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.status = TicketStatus.NOT_SOLD;
    }

    public TicketType getType() {
        return this.type;
    }

    public TicketStatus getStatus() {
        return this.status;
    }

    public void sell() {
        this.status = TicketStatus.SOLD;
    }

    public static class Builder {
        private Long id;
        private TicketType type;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder type(TicketType type) {
            this.type = type;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }
}
