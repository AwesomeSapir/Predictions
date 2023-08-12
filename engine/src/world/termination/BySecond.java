package world.termination;

public class BySecond implements TerminationCondition{

    protected int count;

    public BySecond(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
