package world.value.generator.random;

import world.value.generator.ValueGenerator;

import java.util.Random;

public abstract class RandomValueGenerator<T> implements ValueGenerator<T> {

    protected final Random random;

    public RandomValueGenerator() {
        random = new Random();
    }
}
