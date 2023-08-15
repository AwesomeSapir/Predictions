package world.definition.property;

import world.value.generator.ValueGenerator;

public class BooleanPropertyDefinition extends AbstractPropertyDefinition<Boolean> {
    public BooleanPropertyDefinition(String name, ValueGenerator<Boolean> valueGenerator, boolean isRandomInit) {
        super(name, PropertyType.BOOLEAN, valueGenerator, isRandomInit);
    }
}
