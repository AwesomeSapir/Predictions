package world.rule.action.type.condition;

import engine.prd.PRDAction;
import world.instance.entity.EntityInstance;
import world.rule.action.Action;

import java.util.List;

public class ActionCondition extends Action {

    protected MultiCondition conditions;
    protected List<Action> actionsThen;
    protected List<Action> actionsElse;

    public ActionCondition(PRDAction prdObject) {
        super(prdObject);
        conditions = new MultiCondition(prdObject.getPRDCondition());
        for (PRDAction prdThenAction : prdObject.getPRDThen().getPRDAction()) {
            actionsThen.add(createActionFromPRD(prdThenAction));
        }
        for (PRDAction prdElseAction : prdObject.getPRDElse().getPRDAction()) {
            actionsElse.add(createActionFromPRD(prdElseAction));
        }
    }

    @Override
    public void execute(EntityInstance entity) {
        if(conditions.evaluate(entity)){
            for (Action action : actionsThen) {
                action.execute(entity);
            }
        } else {
            for (Action action : actionsElse) {
                action.execute(entity);
            }
        }
    }
}
