package engine.world.value.generator;

import engine.world.value.generator.fixed.FixedValueGenerator;
import engine.world.type.Range;
import engine.world.value.generator.random.bool.RandomBooleanValueGenerator;
import engine.world.value.generator.random.numeric.RandomDoubleValueGenerator;
import engine.world.value.generator.random.numeric.RandomIntegerValueGenerator;
import engine.world.value.generator.random.string.RandomStringValueGenerator;

public interface ValueGeneratorFactory {

    static <T> ValueGenerator<T> createFixed(T value){
        return new FixedValueGenerator<>(value);
    }

    static ValueGenerator<Boolean> createRandomBoolean() {
        return new RandomBooleanValueGenerator();
    }

    static ValueGenerator<String> createRandomString(){
        return new RandomStringValueGenerator();
    }

    static ValueGenerator<Integer> createRandomInteger(Range range) {
        if (range == null) {
            return new RandomIntegerValueGenerator(Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            return new RandomIntegerValueGenerator((int) range.from(), (int) range.to());
        }
    }

    static ValueGenerator<Double> createRandomDouble(Range range) {
        if (range == null) {
            return new RandomDoubleValueGenerator(Double.MIN_VALUE, Double.MAX_VALUE);
        } else {
            return new RandomDoubleValueGenerator(range.from(), range.to());
        }
    }
}
