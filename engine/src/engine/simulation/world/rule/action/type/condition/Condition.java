package engine.simulation.world.rule.action.type.condition;

import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.Context;
import exception.runtime.IllegalActionException;

public interface Condition {

    boolean evaluate(EntityInstance entityInstance, Context context) throws IllegalActionException;
    boolean evaluate(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context) throws IllegalActionException;
}
