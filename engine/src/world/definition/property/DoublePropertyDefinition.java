package world.definition.property;

import world.value.generator.ValueGenerator;

public class DoublePropertyDefinition extends AbstractPropertyDefinition<Double> {
    public DoublePropertyDefinition(String name, ValueGenerator<Double> valueGenerator) {
        super(name, PropertyType.FLOAT, valueGenerator);
    }
}
