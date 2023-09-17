package engine.simulation.world.termination;

public class ByUser implements TerminationCondition{

    protected boolean stopped;

    public ByUser() {
        stopped = false;
    }

    @Override
    public boolean isMet() {
        return stopped;
    }

    public void stop() {
        this.stopped = true;
    }

    @Override
    public Termination.Type getType() {
        return Termination.Type.USER;
    }

    @Override
    public Boolean getCondition() {
        return true;
    }
}
