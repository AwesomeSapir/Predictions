package world.expression;

import world.instance.entity.EntityInstance;

public interface Expression {
    ExpressionType getType();
    Object getValue(EntityInstance entityInstance);
}
