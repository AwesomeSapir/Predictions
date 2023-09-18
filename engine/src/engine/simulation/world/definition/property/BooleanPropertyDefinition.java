package engine.simulation.world.definition.property;

import engine.simulation.world.ValueType;
import engine.simulation.world.value.generator.ValueGenerator;

public class BooleanPropertyDefinition extends AbstractPropertyDefinition<Boolean> {
    public BooleanPropertyDefinition(String name, ValueGenerator<Boolean> valueGenerator, boolean isRandomInit) {
        super(name, ValueType.BOOLEAN, valueGenerator, isRandomInit);
    }
}
