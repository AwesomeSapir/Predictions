package world.termination;

public class ByTicks implements TerminationCondition{
    protected long count;

    public ByTicks(int count) {
        this.count = count;
    }

    @Override
    public boolean isMet(long compareTo) {
        return compareTo >= count;
    }
}
