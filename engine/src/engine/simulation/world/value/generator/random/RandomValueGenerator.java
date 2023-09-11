package engine.simulation.world.value.generator.random;

import engine.simulation.world.value.generator.ValueGenerator;

import java.io.Serializable;
import java.util.Random;

public abstract class RandomValueGenerator<T> implements ValueGenerator<T>, Serializable {

    protected final Random random;

    public RandomValueGenerator() {
        random = new Random();
    }
}
