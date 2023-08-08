package world.rule.action.type.condition;

import world.instance.entity.EntityInstance;

public interface Condition {

    boolean evaluate(EntityInstance entity);
}
