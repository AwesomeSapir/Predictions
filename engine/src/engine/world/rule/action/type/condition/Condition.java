package engine.world.rule.action.type.condition;

import engine.world.Context;
import engine.world.instance.entity.EntityInstance;

public interface Condition {

    boolean evaluate(EntityInstance entityInstance, Context context);
}
