package world.expression;

import validator.Validator;
import world.definition.property.PropertyType;

public class FreeValueExpression extends AbstractExpression {
    private final PropertyType type;

    public FreeValueExpression(String freeValue, PropertyType type) throws IllegalArgumentException{
        super(ExpressionType.FREE_VALUE);
        this.type = type;
        try {
            switch (type) {
                case DECIMAL:
                    value = Integer.parseInt(freeValue);
                case BOOLEAN:
                    value = Boolean.parseBoolean(freeValue);
                case FLOAT:
                    value = Double.parseDouble(freeValue);
                case STRING:
                    if(Validator.validate(freeValue).isValidString(freeValue).isValid()) {
                        value = freeValue;
                    }
                    default: throw new IllegalArgumentException("Invalid value Expression for property type '" + type + "'");
            }
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Property of a type '" + type + "' can't get the value '" + freeValue +"'.");
        }
    }


    @Override
    public Object getValue() {
       return value;
    }
}
