package engine.simulation.world.termination;

public abstract class ByNumberTermination<T> implements TerminationCondition{

    protected T value;
    protected final T max;

    public ByNumberTermination(T max) {
        this.max = max;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getMax() {
        return max;
    }

    @Override
    public T getCondition() {
        return max;
    }
}
