package engine.simulation.world.rule.action.type.space;

import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.expression.Expression;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.rule.action.ActionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActionProximity extends Action {

    protected final EntityDefinition targetEntity;
    protected final Expression depth;
    protected final List<Action> actions = new ArrayList<>();

    public ActionProximity(EntityDefinition primaryEntity, EntityDefinition secondaryEntity, Expression depth, Collection<Action> actions) {
        super(ActionType.proximity, primaryEntity, null);
        this.targetEntity = secondaryEntity;
        this.depth = depth;
        this.actions.addAll(actions);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        if (entityInstance.getEntityDefinition().equals(primaryEntity)) {
            EntityInstance entityInProximity = context.getSpaceManager().getEntityInProximity(entityInstance.getPoint(), targetEntity, (Double) depth.getValue(entityInstance));
            if (entityInProximity != null) {
                for (Action action : actions) {
                    action.execute(entityInstance, entityInProximity, context);
                }
            }
        }
    }

    @Override
    public void execute(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context) {
        execute(primaryEntity, context);
    }
}
