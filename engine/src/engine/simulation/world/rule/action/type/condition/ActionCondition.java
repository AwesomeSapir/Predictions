package engine.simulation.world.rule.action.type.condition;

import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.rule.action.ActionType;
import engine.simulation.world.rule.action.SecondaryEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActionCondition extends Action {

    protected final MultiCondition conditions;
    protected final List<Action> actionsThen = new ArrayList<>();
    protected final List<Action> actionsElse = new ArrayList<>();

    public ActionCondition(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity, MultiCondition conditions, Collection<Action> actionsThen, Collection<Action> actionsElse) {
        super(ActionType.condition, primaryEntity, secondaryEntity);
        this.conditions = conditions;
        this.actionsThen.addAll(actionsThen);
        this.actionsElse.addAll(actionsElse);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        if(conditions.evaluate(entityInstance, context)){
            for (Action action : actionsThen) {
                action.execute(entityInstance, context);
            }
        } else {
            for (Action action : actionsElse) {
                action.execute(entityInstance, context);
            }
        }
    }

    @Override
    public void execute(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context) {
        if(conditions.evaluate(primaryEntity, secondaryEntity, context)){
            for (Action action : actionsThen) {
                if(action.getType() == ActionType.condition){
                    System.out.println("HEALING");
                }
                action.execute(primaryEntity, secondaryEntity, context);
            }
        } else {
            for (Action action : actionsElse) {
                action.execute(primaryEntity, secondaryEntity, context);
            }
        }
    }

    public MultiCondition getConditions() {
        return conditions;
    }

    public List<Action> getActionsThen() {
        return actionsThen;
    }

    public List<Action> getActionsElse() {
        return actionsElse;
    }
}
