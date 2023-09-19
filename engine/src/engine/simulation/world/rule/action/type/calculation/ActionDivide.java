package engine.simulation.world.rule.action.type.calculation;

import engine.simulation.world.expression.Expression;
import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.rule.action.SecondaryEntity;
import exception.runtime.IllegalActionException;

public class ActionDivide extends ActionCalc {

    public ActionDivide(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity, String resultPropertyName, Expression arg1, Expression arg2) {
        super(primaryEntity, secondaryEntity, resultPropertyName, arg1, arg2);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) throws IllegalActionException {
        double val1 = Double.parseDouble(arg1.getValue(entityInstance).toString());
        double val2 = Double.parseDouble(arg2.getValue(entityInstance).toString());
        if(val2 != 0){
            double result = val1 / val2;
            entityInstance.getPropertyByName(resultPropertyName).setValue(result);
        } else {
            throw new IllegalActionException("Can't divide by zero");
        }
    }

    @Override
    public void execute(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context) throws IllegalActionException {
        double val1 = Double.parseDouble(arg1.getValue(primaryEntity, secondaryEntity).toString());
        double val2 = Double.parseDouble(arg2.getValue(primaryEntity, secondaryEntity).toString());
        if(val2 != 0){
            double result = val1 / val2;
            primaryEntity.getPropertyByName(resultPropertyName).setValue(result);
        } else {
            throw new IllegalActionException("Can't divide by zero");
        }
    }
}
