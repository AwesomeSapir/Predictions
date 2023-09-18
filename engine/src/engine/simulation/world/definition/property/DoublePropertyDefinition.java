package engine.simulation.world.definition.property;

import engine.simulation.world.ValueType;
import engine.simulation.world.type.Range;
import engine.simulation.world.value.generator.ValueGenerator;

public class DoublePropertyDefinition extends AbstractNumericPropertyDefinition<Double> {

    public DoublePropertyDefinition(String name, Range range, ValueGenerator<Double> valueGenerator, boolean isRandomInit) {
        super(name, ValueType.FLOAT, range, valueGenerator, isRandomInit);
    }
}
