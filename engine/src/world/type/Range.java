package world.type;

import engine.prd.PRDRange;

public class Range {
    protected final double to;
    protected final double from;

    public Range(PRDRange prdObject) {
        to = prdObject.getTo();
        from = prdObject.getFrom();
    }

    public boolean isInRange(double num){
        return (num >= from && num <= to);
    }

    public double getTo() {
        return to;
    }

    public double getFrom() {
        return from;
    }
}
