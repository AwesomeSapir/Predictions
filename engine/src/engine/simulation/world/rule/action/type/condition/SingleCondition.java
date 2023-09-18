package engine.simulation.world.rule.action.type.condition;

import engine.simulation.world.expression.Expression;
import engine.simulation.world.Context;
import engine.simulation.world.instance.entity.EntityInstance;
import validator.Validator;

import java.io.Serializable;

public class SingleCondition implements Condition, Serializable {

    protected final Operator operator;
    protected final Expression arg;
    protected final Expression value;

    public SingleCondition(Operator operator, Expression arg, Expression value) {
        this.operator = operator;
        this.arg = arg;
        this.value = value;
    }

    public boolean evaluate(EntityInstance entityInstance, Context context){
        Object argValue = arg.getValue(entityInstance);
        Object expValue = value.getValue(entityInstance);
        if(Validator.validate(argValue.toString()).isDouble().isValid() && Validator.validate(expValue.toString()).isDouble().isValid()){
            double numEntityValue = Double.parseDouble(argValue.toString());
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
                    return !argValue.equals(expValue);
                case eq:
                    return argValue.equals(expValue);
                default:
                    throw new UnsupportedOperationException("Operator " + operator.getOperator() + " not supported");
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String operatorStr = "";
        switch (operator){
            case neq:
                operatorStr = "!=";
                break;
            case eq:
                operatorStr = "==";
                break;
            case bt:
                operatorStr = ">";
                break;
            case lt:
                operatorStr = "<";
                break;
        }
        return arg.toString() + operatorStr + value.toString();
    }
}
