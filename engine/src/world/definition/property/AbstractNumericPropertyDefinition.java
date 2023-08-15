package world.definition.property;

import world.type.Range;
import world.value.generator.ValueGenerator;

public class AbstractNumericPropertyDefinition<T> extends AbstractPropertyDefinition<T>{

    private final Range range;

    public AbstractNumericPropertyDefinition(String name, Range range, ValueGenerator<T> valueGenerator, boolean isRandomInit) {
        super(name, PropertyType.DECIMAL, valueGenerator, isRandomInit);
        this.range = range;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    public Range getRange() {
        return range;
    }
}
