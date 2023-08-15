package world.definition.property;

import world.value.generator.ValueGenerator;

public class StringPropertyDefinition extends AbstractPropertyDefinition<String> {
    public StringPropertyDefinition(String name, ValueGenerator<String> valueGenerator, boolean isRandomInit) {
        super(name, PropertyType.STRING, valueGenerator, isRandomInit);
    }
}
