package world.rule.action.type.condition.single;

import engine.prd.PRDCondition;
import world.Context;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class SingleIntegerCondition extends SingleCondition<Integer>{

    public SingleIntegerCondition(PRDCondition prdObject) {
        super(prdObject);
        value = Integer.valueOf(prdObject.getValue());
    }

    @Override
    public boolean evaluate(Context context) {
        boolean result;
        PropertyInstance property = entity.getPropertyByName(propertyName);
        Integer propertyValue = (Integer) property.getValue();

        switch (operator){
            case neq:
                result = !propertyValue.equals(value);
                break;
            case eq:
                result = propertyValue.equals(value);
                break;
            case bt:
                result = propertyValue > value;
                break;
            case lt:
                result = propertyValue < value;
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator.getOperator());
        }
        return result;
    }
}
