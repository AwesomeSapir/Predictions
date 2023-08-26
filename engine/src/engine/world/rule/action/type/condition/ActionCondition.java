package engine.world.rule.action.type.condition;

import engine.world.Context;
import engine.world.instance.entity.EntityInstance;
import engine.world.rule.action.Action;
import engine.world.rule.action.ActionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActionCondition extends Action {

    protected final MultiCondition conditions;
    protected final List<Action> actionsThen = new ArrayList<>();
    protected final List<Action> actionsElse = new ArrayList<>();

    public ActionCondition(ActionType type, String entityName, MultiCondition conditions, Collection<Action> actionsThen, Collection<Action> actionsElse) {
        super(type, entityName);
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
