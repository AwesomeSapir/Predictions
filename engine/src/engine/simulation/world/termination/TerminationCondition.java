package engine.simulation.world.termination;

public interface TerminationCondition {
    boolean isMet();
    Termination.Type getType();
    Object getCondition();
}
