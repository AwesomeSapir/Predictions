package world.rule.action;

import world.Context;
import world.instance.entity.EntityInstance;

import java.io.Serializable;

public abstract class Action implements Serializable {

    protected final ActionType type;
    protected String entityName;

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

    public void setEntity(String entityName) {
        this.entityName = entityName;
    }
}
