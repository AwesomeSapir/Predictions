package engine.simulation.world.termination;

public class BySecond extends ByNumberTermination<Long> {

    public BySecond(long max) {
        super(max);
        value = 0L;
    }

    @Override
    public boolean isMet() {
        return value >= max;
    }

    @Override
    public Termination.Type getType() {
        return Termination.Type.SECONDS;
    }
}
