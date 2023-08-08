package world.definition.property;

import world.type.Range;
import world.value.generator.ValueGenerator;

public class IntegerPropertyDefinition extends AbstractNumericPropertyDefinition<Integer> {

    public IntegerPropertyDefinition(String name, Range range, ValueGenerator<Integer> valueGenerator) {
        super(name, range, valueGenerator);
    }
}
