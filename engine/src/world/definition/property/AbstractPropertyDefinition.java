package world.definition.property;

import world.type.Range;
import world.value.generator.ValueGenerator;

public abstract class AbstractPropertyDefinition<T> implements PropertyDefinition{

    private final String name;
    private final PropertyType type;
    private final ValueGenerator<T> valueGenerator;

    private final boolean isRandomInit;

    public AbstractPropertyDefinition(String name, PropertyType type, ValueGenerator<T> valueGenerator, boolean isRandomInit) {
        this.name = name;
        this.type = type;
        this.valueGenerator = valueGenerator;
        this.isRandomInit = isRandomInit;
    }

    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    public T generateValue() {
        return valueGenerator.generateValue();
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isRandomInit() {
        return isRandomInit;
    }

    @Override
    public Range getRange() {
        return null;
    }
}
