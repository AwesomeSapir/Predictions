package engine.simulation.world.expression.auxiliary;

import engine.simulation.world.Context;
import engine.simulation.world.ValueType;
import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.Expression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;
import exception.runtime.IllegalActionException;

public class PercentExpression extends AbstractExpression {

    protected Expression arg;
    protected Expression percentage;

    public PercentExpression(Expression arg, Expression percentage) {
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.arg = arg;
        this.percentage = percentage;
    }

    @Override
    public Object getValue(EntityInstance entityInstance, Context context) throws IllegalActionException {
        Object argObj = arg.getValue(entityInstance, context);
        Object percentageObj = percentage.getValue(entityInstance, context);
        if(argObj!= null && percentageObj != null) {
            double argValue = (double) argObj;
            double percentageValue = (double) percentageObj;
            return argValue * percentageValue / 100;
        }
        return null;
    }

    @Override
    public Object getValue(Context context, EntityInstance... entityInstances) throws IllegalActionException {
        for (EntityInstance entityInstance : entityInstances){
            Object result = getValue(entityInstance, context);
            if(result != null){
                return result;
            }
        }
        throw new IllegalActionException("Entity instances don't match defined definition");
    }

    @Override
    public ValueType getValueType() {
        return ValueType.FLOAT;
    }
}
