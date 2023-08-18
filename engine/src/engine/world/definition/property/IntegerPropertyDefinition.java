package engine.world.definition.property;

import engine.world.type.Range;
import engine.world.value.generator.ValueGenerator;

public class IntegerPropertyDefinition extends AbstractNumericPropertyDefinition<Integer> {

    public IntegerPropertyDefinition(String name, Range range, ValueGenerator<Integer> valueGenerator, boolean isRandomInit) {
        super(name, PropertyType.DECIMAL, range, valueGenerator, isRandomInit);
    }
}
