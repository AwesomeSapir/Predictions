package engine.world.rule.action.type;

import engine.world.Context;
import engine.world.definition.entity.EntityDefinition;
import engine.world.instance.entity.EntityInstance;
import engine.world.rule.action.Action;
import engine.world.rule.action.ActionType;

public class ActionKill extends Action {

    public ActionKill(ActionType type, EntityDefinition entity) {
        super(type, entity);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        context.removeEntity(entityInstance);
    }
}
