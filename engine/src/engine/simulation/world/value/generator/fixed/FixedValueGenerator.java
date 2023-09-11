package engine.simulation.world.value.generator.fixed;

import engine.simulation.world.value.generator.ValueGenerator;

import java.io.Serializable;

public class FixedValueGenerator<T> implements ValueGenerator<T>, Serializable {

    private final T fixedValue;

    public FixedValueGenerator(T fixedValue) {
        this.fixedValue = fixedValue;
    }

    @Override
    public T generateValue() {
        return fixedValue;
    }
}
