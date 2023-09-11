package engine.simulation.world.expression;

import engine.simulation.world.instance.entity.EntityInstance;

public class FreeValueExpression extends AbstractExpression {
    private final Object value;

    public FreeValueExpression(Object value) {
        super(ExpressionType.FREE_VALUE);
        this.value = value;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
