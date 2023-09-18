package engine.simulation.world.definition.property;

import engine.simulation.world.ValueType;
import engine.simulation.world.type.Range;
import engine.simulation.world.value.generator.ValueGenerator;

public class AbstractNumericPropertyDefinition<T> extends AbstractPropertyDefinition<T>{

    private final Range range;

    public AbstractNumericPropertyDefinition(String name, ValueType type, Range range, ValueGenerator<T> valueGenerator, boolean isRandomInit) {
        super(name, type, valueGenerator, isRandomInit);
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
