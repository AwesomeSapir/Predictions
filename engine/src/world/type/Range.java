package world.type;

public class Range {

    protected final double from;
    protected final double to;

    public Range(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public boolean isInRange(double num){
        return (num >= from && num <= to);
    }

    public double to() {
        return to;
    }

    public double from() {
        return from;
    }
}
