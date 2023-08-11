package world.rule.action.type.condition.single;

import engine.prd.PRDCondition;
import world.Context;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class SingleBooleanCondition extends SingleCondition<Boolean>{

    public SingleBooleanCondition(PRDCondition prdObject) {
        super(prdObject);
        value = Boolean.valueOf(prdObject.getValue());
    }

    @Override
    public boolean evaluate(Context context) {
        boolean result;
        PropertyInstance property = entity.getPropertyByName(propertyName);
        Boolean propertyValue = (Boolean) property.getValue();

        switch (operator){
            case neq:
                result = !propertyValue.equals(value);
                break;
            case eq:
                result = propertyValue.equals(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator.getOperator());
        }
        return result;
    }
}
