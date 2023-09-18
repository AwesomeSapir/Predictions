package engine.simulation.world.expression.auxiliary;

import engine.simulation.world.ValueType;
import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.Expression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;

public class PercentExpression extends AbstractExpression {

    protected Expression arg;
    protected Expression percentage;

    public PercentExpression(Expression arg, Expression percentage) {
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.arg = arg;
        this.percentage = percentage;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        Object argObj = arg.getValue(entityInstance);
        Object percentageObj = percentage.getValue(entityInstance);
        if(argObj!= null && percentageObj != null) {
            double argValue = (double) argObj;
            double percentageValue = (double) percentageObj;
            return argValue * percentageValue / 100;
        }
        return null;
    }

    @Override
    public Object getValue(EntityInstance... entityInstances) {
        for (EntityInstance entityInstance : entityInstances){
            Object result = getValue(entityInstance);
            if(result != null){
                return result;
            }
        }
        throw new RuntimeException("Entity instances don't match defined definition");
    }

    @Override
    public ValueType getValueType() {
        return ValueType.FLOAT;
    }
}
