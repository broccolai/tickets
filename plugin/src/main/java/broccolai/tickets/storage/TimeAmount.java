package broccolai.tickets.storage;

public enum TimeAmount {
    DAY(86400L), WEEK(604800L), MONTH(2628000L), YEAR(31556952L), FOREVER(null);

    private final Long length;

    TimeAmount(Long length) {
        this.length = length;
    }

    public Long getLength() {
        return length;
    }
}