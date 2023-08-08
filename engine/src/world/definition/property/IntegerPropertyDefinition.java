package world.definition.property;

import world.value.generator.ValueGenerator;

public class IntegerPropertyDefinition extends AbstractPropertyDefinition<Integer> {
    public IntegerPropertyDefinition(String name, ValueGenerator<Integer> valueGenerator) {
        super(name, PropertyType.DECIMAL, valueGenerator);
    }
}
