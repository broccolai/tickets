package love.broccolai.tickets.api.model;

public enum TicketStatus {
    OPEN(0x69F0AE),
    PICKED(0xFFFF00),
    CLOSED(0xFF5252);

    private final int color;

    TicketStatus(int color) {
        this.color = color;
    }

    public int color() {
        return this.color;
    }
}
