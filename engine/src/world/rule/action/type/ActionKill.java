package world.rule.action.type;

import world.Context;
import world.instance.entity.EntityInstance;
import world.rule.action.Action;
import world.rule.action.ActionType;

public class ActionKill extends Action {

    public ActionKill(ActionType type, String entityName) {
        super(type, entityName);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        context.removeEntity(entityInstance);
    }
}
