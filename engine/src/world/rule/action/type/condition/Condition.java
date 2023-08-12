package world.rule.action.type.condition;

import world.Context;
import world.instance.entity.EntityInstance;

public interface Condition {

    boolean evaluate(EntityInstance entityInstance, Context context);
}
