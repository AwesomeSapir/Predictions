package engine.simulation.world.expression;

import engine.simulation.world.ValueType;
import engine.simulation.world.instance.entity.EntityInstance;
import exception.runtime.IllegalActionException;

public interface Expression {
    ExpressionType getType();
    Object getValue(EntityInstance entityInstance) throws IllegalActionException;
    Object getValue(EntityInstance... entityInstances) throws IllegalActionException;
    ValueType getValueType();
}
