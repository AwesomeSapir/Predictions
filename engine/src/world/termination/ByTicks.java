package world.termination;

import engine.prd.PRDByTicks;

public class ByTicks implements TerminationCondition{
    protected int count;

    public ByTicks(PRDByTicks prdObject) {
        count = prdObject.getCount();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
