package engine.world.rule.action.type.calculation;

import engine.world.definition.entity.EntityDefinition;
import engine.world.expression.Expression;
import engine.world.Context;
import engine.world.instance.entity.EntityInstance;
import engine.world.rule.action.ActionType;

public class ActionMultiply extends ActionCalc {

    public ActionMultiply(ActionType type, EntityDefinition entity, String resultPropertyName, Expression arg1, Expression arg2) {
        super(type, entity, resultPropertyName, arg1, arg2);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        double val1 = Double.parseDouble(arg1.getValue(entityInstance).toString());
        double val2 = Double.parseDouble(arg2.getValue(entityInstance).toString());
        double result = val1 * val2;

        entityInstance.getPropertyByName(resultPropertyName).setValue(result);
    }
}
