package world.termination;

import engine.prd.PRDBySecond;

public class BySecond implements TerminationCondition{

    protected int count;

    public BySecond(PRDBySecond prdObject) {
        count = prdObject.getCount();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
