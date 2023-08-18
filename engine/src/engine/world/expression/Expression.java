package engine.world.expression;

import engine.world.instance.entity.EntityInstance;

public interface Expression {
    ExpressionType getType();
    Object getValue(EntityInstance entityInstance);
}
