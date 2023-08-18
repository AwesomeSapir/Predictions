package engine.world.value.generator.random.bool;

import engine.world.value.generator.random.RandomValueGenerator;

public class RandomBooleanValueGenerator extends RandomValueGenerator<Boolean> {
    @Override
    public Boolean generateValue() {
        return random.nextBoolean();
    }
}
