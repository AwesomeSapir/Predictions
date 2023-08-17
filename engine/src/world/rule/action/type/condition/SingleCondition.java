package world.rule.action.type.condition;

import world.Context;
import world.expression.Expression;
import world.instance.entity.EntityInstance;

public class SingleCondition implements Condition {

    protected Operator operator;
    protected String propertyName;
    protected Expression value;

    public SingleCondition(Operator operator, String propertyName, Expression value) {
        this.operator = operator;
        this.propertyName = propertyName;
        this.value = value;
    }

    public boolean evaluate(EntityInstance entityInstance, Context context){
        Object entityValue = entityInstance.getPropertyByName(propertyName).getValue();
        Object expValue = value.getValue(entityInstance);
        if(context.getPrimaryEntityDefinition().getProperties().get(propertyName).isNumeric()){
            double numEntityValue = Double.parseDouble(entityValue.toString());
            double numExpValue = Double.parseDouble(expValue.toString());
            switch (operator){
                case neq:
                    return numEntityValue != numExpValue;
                case eq:
                    return numEntityValue == numExpValue;
                case bt:
                    return numEntityValue > numExpValue;
                case lt:
                    return numEntityValue < numExpValue;
            }
        } else {
            switch (operator){
                case neq:
                    return !entityValue.equals(expValue);
                case eq:
                    return entityValue.equals(expValue);
                default:
                    throw new UnsupportedOperationException("Operator " + operator.getOperator() + " not supported");
            }
        }
        return false;
    }
}
