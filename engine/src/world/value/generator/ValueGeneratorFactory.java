package world.value.generator;

import world.value.generator.fixed.FixedValueGenerator;
import world.value.generator.random.bool.RandomBooleanValueGenerator;
import world.value.generator.random.numeric.RandomDoubleValueGenerator;
import world.value.generator.random.numeric.RandomIntegerValueGenerator;
import world.value.generator.random.string.RandomStringValueGenerator;

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

    static ValueGenerator<Integer> createRandomInteger(Integer from, Integer to){
        return new RandomIntegerValueGenerator(from, to);
    }

    static ValueGenerator<Double> createRandomDouble(Double from, Double to){
        return new RandomDoubleValueGenerator(from, to);
    }
}
