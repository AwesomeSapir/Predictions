package world.termination;

import java.io.Serializable;

public class BySecond implements TerminationCondition, Serializable {

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
