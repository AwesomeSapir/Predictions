package engine.simulation.world.expression;

import engine.simulation.world.Context;
import engine.simulation.world.ValueType;
import engine.simulation.world.instance.entity.EntityInstance;
import exception.runtime.IllegalActionException;

public interface Expression {
    ExpressionType getType();
    Object getValue(EntityInstance entityInstance, Context context) throws IllegalActionException;
    Object getValue(Context context, EntityInstance... entityInstances) throws IllegalActionException;
    ValueType getValueType();
}
