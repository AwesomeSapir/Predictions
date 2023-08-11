package world.expression;

import world.instance.property.PropertyInstance;
import world.Context;

import java.util.Random;

public abstract class AbstractExpression implements Expression{
    private final ExpressionType type;

    protected Object value;

    public AbstractExpression(ExpressionType type) {
        this.type = type;
    }

    @Override
    public ExpressionType getType() {
        return type;
    }
    public abstract Object getValue();

}
