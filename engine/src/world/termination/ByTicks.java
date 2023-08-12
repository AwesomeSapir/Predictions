package world.termination;

public class ByTicks implements TerminationCondition{
    protected int count;

    public ByTicks(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
