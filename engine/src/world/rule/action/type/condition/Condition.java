package world.rule.action.type.condition;

import world.Entity;

public interface Condition {

    boolean evaluate(Entity entity);
}
