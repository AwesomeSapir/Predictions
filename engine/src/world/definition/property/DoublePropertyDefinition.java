package world.definition.property;

import world.type.Range;
import world.value.generator.ValueGenerator;

public class DoublePropertyDefinition extends AbstractNumericPropertyDefinition<Double> {

    public DoublePropertyDefinition(String name, Range range, ValueGenerator<Double> valueGenerator) {
        super(name, range, valueGenerator);
    }
}
