package dto.detail;

public class DTORange {
    private final double from;
    private final double to;

    public DTORange(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }
}
