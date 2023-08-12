package world.rule.action.type.condition;

import world.Context;
import world.instance.entity.EntityInstance;
import world.rule.action.Action;
import world.rule.action.ActionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActionCondition extends Action {

    protected MultiCondition conditions;
    protected List<Action> actionsThen = new ArrayList<>();
    protected List<Action> actionsElse = new ArrayList<>();

    public ActionCondition(ActionType type, String entityName, MultiCondition conditions, Collection<Action> actionsThen, Collection<Action> actionsElse) {
        super(type, entityName);
        this.conditions = conditions;
        this.actionsThen.addAll(actionsThen);
        this.actionsElse.addAll(actionsElse);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        if(conditions.evaluate(context)){
            for (Action action : actionsThen) {
                action.execute(entityInstance, context);
            }
        } else {
            for (Action action : actionsElse) {
                action.execute(entityInstance, context);
            }
        }
    }
}
