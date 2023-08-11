package world.rule.action.type.condition;

import engine.prd.PRDAction;
import world.Context;
import world.instance.entity.EntityInstance;
import world.rule.action.Action;

import java.util.List;

public class ActionCondition extends Action {

    protected MultiCondition conditions;
    protected List<Action> actionsThen;
    protected List<Action> actionsElse;

    public ActionCondition(PRDAction prdObject, Context context) {
        super(prdObject);
        conditions = new MultiCondition(prdObject.getPRDCondition(), context);
        for (PRDAction prdThenAction : prdObject.getPRDThen().getPRDAction()) {
            actionsThen.add(createActionFromPRD(prdThenAction));
        }
        for (PRDAction prdElseAction : prdObject.getPRDElse().getPRDAction()) {
            actionsElse.add(createActionFromPRD(prdElseAction));
        }
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) throws Exception{
        if(conditions.evaluate(context)){
            for (Action action : actionsThen) {
                action.execute(entityInstance,context);
            }
        } else {
            for (Action action : actionsElse) {
                action.execute(entityInstance,context);
            }
        }
    }
}
