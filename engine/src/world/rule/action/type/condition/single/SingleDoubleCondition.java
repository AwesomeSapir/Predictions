package world.rule.action.type.condition.single;

import engine.prd.PRDCondition;
import world.Context;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class SingleDoubleCondition extends SingleCondition<Double>{

    public SingleDoubleCondition(PRDCondition prdObject) {
        super(prdObject);
        value = Double.valueOf(prdObject.getValue());
    }

    @Override
    public boolean evaluate(Context context) {
        boolean result;
        PropertyInstance property = entity.getPropertyByName(propertyName);
        Double propertyValue = (Double) property.getValue();

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
