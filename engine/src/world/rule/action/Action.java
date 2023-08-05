package world.rule.action;

import engine.prd.PRDAction;
import world.Entity;

public abstract class Action {

    protected final ActionType type;
    protected String entityName;

    public Action(PRDAction prdObject) {
        type = ActionType.valueOf(prdObject.getType());
        entityName =  prdObject.getEntity();
    }

    public abstract void execute(Entity entity);

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
