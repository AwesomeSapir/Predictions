package world.definition.property;

import world.type.Range;
import world.value.generator.ValueGenerator;
import world.value.generator.ValueGeneratorFactory;

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

    @Override
    public boolean isNumeric() {
        return false;
    }

    public static PropertyDefinition createPropertyDefinitionByType(String name, PropertyType type, String init, Range range, boolean isRandomInit){
        PropertyDefinition propertyDefinition;
        switch (type) {
            case DECIMAL: {
                ValueGenerator<Integer> valueGenerator;
                if (!isRandomInit) {
                    int value = Integer.parseInt(init);
                    valueGenerator = ValueGeneratorFactory.createFixed(value);
                } else {
                    valueGenerator = ValueGeneratorFactory.createRandomInteger(range);
                }
                propertyDefinition = new IntegerPropertyDefinition(name, range, valueGenerator);
                break;
            }
            case BOOLEAN: {
                propertyDefinition = new BooleanPropertyDefinition(name, ValueGeneratorFactory.createRandomBoolean());
                break;
            }
            case FLOAT: {
                ValueGenerator<Double> valueGenerator;
                if (!isRandomInit) {
                    double value = Double.parseDouble(init);
                    valueGenerator = ValueGeneratorFactory.createFixed(value);
                } else {
                    valueGenerator = ValueGeneratorFactory.createRandomDouble(range);
                }
                propertyDefinition = new DoublePropertyDefinition(name, range, valueGenerator);
                break;
            }
            case STRING: {
                propertyDefinition = new StringPropertyDefinition(name, ValueGeneratorFactory.createRandomString());
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid property type");
        }
        return propertyDefinition;
    }
}
