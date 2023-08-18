package engine.world.rule.action;

import engine.world.Context;
import engine.world.instance.entity.EntityInstance;

import java.io.Serializable;

public abstract class Action implements Serializable {

    protected final ActionType type;
    protected final String entityName;

    public Action(ActionType type, String entityName) {
        this.type = type;
        this.entityName = entityName;
    }

    public abstract void execute(EntityInstance entityInstance, Context context);

    public ActionType getType() {
        return type;
    }

    public String getEntity() {
        return entityName;
    }
}
