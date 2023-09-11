package engine.simulation.world.value.generator.random.numeric;

import engine.simulation.world.value.generator.random.RandomValueGenerator;

public class RandomIntegerValueGenerator extends RandomValueGenerator<Integer> {

    protected final Integer from;
    protected final Integer to;

    public RandomIntegerValueGenerator(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Integer generateValue() {
        return from + random.nextInt(to - from + 1);
    }
}
