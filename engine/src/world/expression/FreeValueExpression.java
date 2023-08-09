package world.expression;

import world.definition.property.PropertyType;

public class FreeValueExpression extends AbstractExpression {
    private final String value;
    private final PropertyType type;

    public FreeValueExpression(String value, PropertyType type) {
        super(ExpressionType.FREE_VALUE);
        this.value = value;
        this.type = type;
    }


    @Override
    public Object getValue() {
        switch (type) {
            case DECIMAL:
                return Integer.parseInt(value);
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            case FLOAT:
                return Double.parseDouble(value);
            case STRING:
                return value;
            default: throw new IllegalArgumentException("Invalid property type for Free Value Expression");
        }
    }
}
