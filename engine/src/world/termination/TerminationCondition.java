package world.termination;

public interface TerminationCondition {
    boolean isMet(long compareTo);
}
