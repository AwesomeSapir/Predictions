package engine.simulation.world.expression;

import engine.simulation.world.definition.property.PropertyType;
import engine.simulation.world.instance.entity.EntityInstance;

public interface Expression {
    ExpressionType getType();
    Object getValue(EntityInstance entityInstance);
    PropertyType getValueType();
}
