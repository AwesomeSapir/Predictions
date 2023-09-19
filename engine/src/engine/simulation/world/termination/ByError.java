package engine.simulation.world.termination;

public class ByError implements TerminationCondition{

    public ByError() {
    }

    @Override
    public boolean isMet() {
        return true;
    }

    @Override
    public Termination.Type getType() {
        return Termination.Type.ERROR;
    }

    @Override
    public Boolean getCondition() {
        return true;
    }

}
