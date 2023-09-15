package engine.simulation.world.expression.auxiliary;

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
        try {
            double argValue = (double) arg.getValue(entityInstance);
            double percentageValue = (double) percentage.getValue(entityInstance);

            return argValue*percentageValue/100;
        } catch (Exception e){
            throw new RuntimeException("PercentExpression received non numeric values");
        }
    }
}
