package world.rule.action.type.condition;

import engine.prd.PRDAction;
import world.rule.action.Action;

import java.util.List;

public abstract class ActionCondition extends Action {

    protected MultiCondition conditions;
    protected List<Action> actionsThen;
    protected List<Action> actionsElse;

    public ActionCondition(PRDAction prdObject) {
        super(prdObject);
        Singularity singularity = Singularity.valueOf(prdObject.getPRDCondition().getSingularity());
        if (singularity == Singularity.multiple){
            case single:
                prdObject.getPRDCondition().getPRDCondition();
                break;
            case multiple:
                prdObject.getPRDCondition().getPRDCondition();
                break;
        }
    }
}
