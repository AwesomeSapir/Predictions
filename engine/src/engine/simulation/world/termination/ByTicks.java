package engine.simulation.world.termination;

public class ByTicks extends ByNumberTermination<Integer> {

    public ByTicks(int max) {
        super(max);
        value = 0;
    }

    @Override
    public boolean isMet() {
        return value >= max;
    }

    @Override
    public Termination.Type getType() {
        return Termination.Type.TICKS;
    }
}
