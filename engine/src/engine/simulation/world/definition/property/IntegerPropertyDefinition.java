package engine.simulation.world.definition.property;

import engine.simulation.world.ValueType;
import engine.simulation.world.type.Range;
import engine.simulation.world.value.generator.ValueGenerator;

public class IntegerPropertyDefinition extends AbstractNumericPropertyDefinition<Integer> {

    public IntegerPropertyDefinition(String name, Range range, ValueGenerator<Integer> valueGenerator, boolean isRandomInit) {
        super(name, ValueType.DECIMAL, range, valueGenerator, isRandomInit);
    }
}
