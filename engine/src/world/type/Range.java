package world.type;

import com.sun.istack.internal.NotNull;
import engine.prd.PRDRange;

public class Range {

    protected final double from;
    protected final double to;

    public Range(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public Range(@NotNull PRDRange prdObject){
        this.from = prdObject.getFrom();
        this.to = prdObject.getTo();
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
