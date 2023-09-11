package engine.simulation.world.expression;

import engine.simulation.world.instance.entity.EntityInstance;

import java.io.Serializable;

public abstract class AbstractExpression implements Expression, Serializable {
    private final ExpressionType type;

    public AbstractExpression(ExpressionType type) {
        this.type = type;
    }

    @Override
    public ExpressionType getType() {
        return type;
    }
    public abstract Object getValue(EntityInstance entityInstance);
}
