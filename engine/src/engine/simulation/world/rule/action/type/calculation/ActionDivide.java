package engine.simulation.world.rule.action.type.calculation;

import engine.simulation.world.expression.Expression;
import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;

public class ActionDivide extends ActionCalc {

    public ActionDivide(EntityDefinition primaryEntity, EntityDefinition secondaryEntity, String resultPropertyName, Expression arg1, Expression arg2) {
        super(primaryEntity, secondaryEntity, resultPropertyName, arg1, arg2);
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
