package engine.simulation.world.expression;

import engine.simulation.world.ValueType;
import engine.simulation.world.instance.entity.EntityInstance;

public interface Expression {
    ExpressionType getType();
    Object getValue(EntityInstance entityInstance);
    ValueType getValueType();
}
