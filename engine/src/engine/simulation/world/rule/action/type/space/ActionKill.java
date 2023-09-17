package engine.simulation.world.rule.action.type.space;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.Context;
import engine.simulation.world.rule.action.ActionType;
import engine.simulation.world.rule.action.SecondaryEntity;

public class ActionKill extends Action {

    public ActionKill(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) {
        super(ActionType.kill, primaryEntity, secondaryEntity);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        context.removeEntity(entityInstance);
    }
}
