package engine.simulation.world.definition.property;

import engine.simulation.world.ValueType;
import engine.simulation.world.value.generator.ValueGenerator;

public class StringPropertyDefinition extends AbstractPropertyDefinition<String> {
    public StringPropertyDefinition(String name, ValueGenerator<String> valueGenerator, boolean isRandomInit) {
        super(name, ValueType.STRING, valueGenerator, isRandomInit);
    }
}
