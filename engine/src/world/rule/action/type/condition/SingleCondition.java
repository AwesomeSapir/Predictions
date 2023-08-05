package world.rule.action.type.condition;

import world.Entity;
import world.property.Property;
import world.type.Value;

public class SingleCondition implements Condition {

    protected Entity entity;
    protected Operator operator;
    protected String propertyName;
    protected String value;

    @Override
    public boolean evaluate() {
        boolean result = false;
        Property property = entity.getPropertyMap().get(propertyName);
        Value val1 = property.getPropertyValue();
        Value val2 = new Value(value, property.getType(), property.getRange());

        switch (operator){
            case neq:
                result = !val1.equals(val2);
                break;
            case eq:
                result = val1.equals(val2);
                break;
            case bt:
                result = val1.biggerThan(val2);
                break;
            case lt:
                result = val1.lessThan(val2);
                break;
        }
        return result;
    }
}
