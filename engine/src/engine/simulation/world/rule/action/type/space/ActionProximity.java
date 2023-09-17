package engine.simulation.world.rule.action.type.space;

import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.rule.action.ActionType;
import engine.simulation.world.rule.action.SecondaryEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActionProximity extends Action {

    protected final int depth;
    protected final List<Action> actions = new ArrayList<>();

    public ActionProximity(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity, int depth, Collection<Action> actions) {
        super(ActionType.proximity, primaryEntity, secondaryEntity);
        this.depth = depth;
        this.actions.addAll(actions);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        if(entityInstance.getEntityDefinition().equals(primaryEntity)){
            EntityInstance entityInProximity = context.getSpaceManager().getEntityInProximity(entityInstance.getPoint(), secondaryEntity.getEntityDefinition(), depth);
            for (Action action : actions){
                action.execute(entityInProximity, context);
            }
        }
    }
}
