package engine.simulation.world.termination;

import java.io.Serializable;

public class ByTicks implements TerminationCondition, Serializable {
    protected final long count;

    public ByTicks(int count) {
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
