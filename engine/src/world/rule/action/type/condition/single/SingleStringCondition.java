package world.rule.action.type.condition.single;

import engine.prd.PRDCondition;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class SingleStringCondition extends SingleCondition<String>{

    public SingleStringCondition(PRDCondition prdObject) {
        super(prdObject);
        value = prdObject.getValue();
    }

    @Override
    public boolean evaluate(EntityInstance entity) {
        boolean result;
        PropertyInstance property = entity.getPropertyByName(propertyName);
        String propertyValue = (String) property.getValue();

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
