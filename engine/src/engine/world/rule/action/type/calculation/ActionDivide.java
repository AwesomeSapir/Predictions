package engine.world.rule.action.type.calculation;

import engine.world.Context;
import engine.world.expression.Expression;
import engine.world.instance.entity.EntityInstance;
import engine.world.rule.action.ActionType;

public class ActionDivide extends ActionCalc {

    public ActionDivide(ActionType type, String entityName, String resultPropertyName, Expression arg1, Expression arg2) {
        super(type, entityName, resultPropertyName, arg1, arg2);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        double val1 = Double.parseDouble(arg1.getValue(entityInstance).toString());
        double val2 = Double.parseDouble(arg2.getValue(entityInstance).toString());
        if(val2 != 0){
            double result = val1 / val2;
            entityInstance.getPropertyByName(resultPropertyName).setValue(result);
        } else {
            throw new IllegalArgumentException("Can't divide by zero");
        }
    }
}
