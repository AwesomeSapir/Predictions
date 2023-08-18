package engine.world.termination;

public interface TerminationCondition {
    boolean isMet(long compareTo);
    long getCount();
}
