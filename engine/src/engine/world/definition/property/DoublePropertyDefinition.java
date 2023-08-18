package engine.world.definition.property;

import engine.world.type.Range;
import engine.world.value.generator.ValueGenerator;

public class DoublePropertyDefinition extends AbstractNumericPropertyDefinition<Double> {

    public DoublePropertyDefinition(String name, Range range, ValueGenerator<Double> valueGenerator, boolean isRandomInit) {
        super(name, PropertyType.FLOAT, range, valueGenerator, isRandomInit);
    }
}
