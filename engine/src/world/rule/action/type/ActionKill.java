package world.rule.action.type;

import engine.prd.PRDAction;
import world.instance.entity.EntityInstance;
import world.rule.action.Action;

public class ActionKill extends Action {

    public ActionKill(PRDAction prdObject) {
        super(prdObject);
    }

    @Override
    public void execute(EntityInstance entity) {
        //TODO decide how to implement
    }
}
