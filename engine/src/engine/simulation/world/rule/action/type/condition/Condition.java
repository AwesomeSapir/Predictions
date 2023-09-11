package engine.simulation.world.rule.action.type.condition;

import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.Context;

public interface Condition {

    boolean evaluate(EntityInstance entityInstance, Context context);
}
