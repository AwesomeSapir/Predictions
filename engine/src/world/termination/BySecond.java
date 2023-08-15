package world.termination;

public class BySecond implements TerminationCondition{

    protected long count;

    public BySecond(int count) {
        this.count = count;
    }

    @Override
    public boolean isMet(long compareTo) {
        return compareTo >= count;
    }

    @Override
    public long getCount() {
        return count;
    }
}
