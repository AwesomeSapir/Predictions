package world.value.generator.random.numeric;

import world.value.generator.random.RandomValueGenerator;

public class RandomDoubleValueGenerator extends RandomValueGenerator<Double> {

    protected final Double from;
    protected final Double to;

    public RandomDoubleValueGenerator(Double from, Double to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Double generateValue() {
        return from + random.nextDouble() * (to - from);
    }
}
