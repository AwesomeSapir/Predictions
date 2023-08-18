package world.expression;

import world.instance.entity.EntityInstance;

import java.io.Serializable;

public abstract class AbstractExpression implements Expression, Serializable {
    private final ExpressionType type;

    protected Object value;

    public AbstractExpression(ExpressionType type) {
        this.type = type;
    }

    @Override
    public ExpressionType getType() {
        return type;
    }
    public abstract Object getValue(EntityInstance entityInstance);

}
