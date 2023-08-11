package world.rule.action;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.instance.entity.EntityInstance;
import world.rule.action.type.value.ActionDecrease;
import world.rule.action.type.value.ActionIncrease;
import world.rule.action.type.ActionKill;
import world.rule.action.type.value.ActionSet;
import world.rule.action.type.calculation.ActionDivide;
import world.rule.action.type.calculation.ActionMultiply;
import world.rule.action.type.condition.ActionCondition;

public abstract class Action {

    protected final ActionType type;
    protected String entityName;

    public Action(PRDAction prdObject) {
        type = ActionType.valueOf(prdObject.getType());
        entityName =  prdObject.getEntity();
    }

    public abstract void execute(EntityInstance entityInstance, Context context) throws Exception;

    public ActionType getType() {
        return type;
    }

    public String getEntity() {
        return entityName;
    }

    public void setEntity(String entityName) {
        this.entityName = entityName;
    }

    public static Action createActionFromPRD(PRDAction prdAction, Context context){
        ActionType type = ActionType.valueOf(prdAction.getType());
        Action action = null;
        switch (type) {
            case calculation:
                if (prdAction.getPRDMultiply() != null) {
                    action = new ActionMultiply(prdAction, context);
                } else if (prdAction.getPRDDivide() != null) {
                    action = new ActionDivide(prdAction, context);
                }
                break;
            case condition:
                action = new ActionCondition(prdAction, context);
                break;
            case decrease:
                action = new ActionDecrease(prdAction, context);
                break;
            case increase:
                action = new ActionIncrease(prdAction, context);
                break;
            case kill:
                action = new ActionKill(prdAction);
                break;
            case set:
                action = new ActionSet(prdAction, context);
                break;
        }
        return action;
    }

}
