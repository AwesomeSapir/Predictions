package world.definition.property;

import world.value.generator.ValueGenerator;

public abstract class AbstractPropertyDefinition<T> implements PropertyDefinition{

    private final String name;
    private final PropertyType type;
    private final ValueGenerator<T> valueGenerator;

    public AbstractPropertyDefinition(String name, PropertyType type, ValueGenerator<T> valueGenerator) {
        this.name = name;
        this.type = type;
        this.valueGenerator = valueGenerator;
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
}
