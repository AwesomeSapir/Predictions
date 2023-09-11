package engine.simulation.world.termination;

public interface TerminationCondition {
    boolean isMet(long compareTo);
    long getCount();
}
