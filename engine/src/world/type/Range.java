package world.type;

import java.io.Serializable;

public class Range implements Serializable {

    protected final double from;
    protected final double to;

    public Range(double from, double to) {
        if(from > to){
            throw new IllegalArgumentException("Range is invalid, from: " + from + " is bigger than to: " + to + ".");
        }
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
