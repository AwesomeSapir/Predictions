package world.rule;

import engine.prd.PRDAction;
import engine.prd.PRDRule;
import world.rule.action.Action;
import world.rule.action.ActionType;
import world.rule.action.type.ActionDecrease;
import world.rule.action.type.ActionIncrease;
import world.rule.action.type.ActionKill;
import world.rule.action.type.ActionSet;
import world.rule.action.type.calculation.ActionDivide;
import world.rule.action.type.calculation.ActionMultiply;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    protected String name;
    protected Activation activation;
    protected List<Action> actions = new ArrayList<>();

    public Rule(PRDRule prdObject) {
        name = prdObject.getName();
        activation = new Activation(prdObject.getPRDActivation());
        for (PRDAction prdAction : prdObject.getPRDActions().getPRDAction()) {
            ActionType type = ActionType.valueOf(prdAction.getType());
            Action action;
            switch (type){
                case calculation:
                    if(prdAction.getPRDMultiply() != null){
                        action = new ActionMultiply(prdAction);
                    } else if (prdAction.getPRDDivide() != null){
                        action = new ActionDivide(prdAction);
                    }
                    break;
                case condition:

                    break;
                case decrease:
                    action = new ActionDecrease(prdAction);
                    break;
                case increase:
                    action = new ActionIncrease(prdAction);
                    break;
                case kill:
                    action = new ActionKill(prdAction);
                    break;
                case set:
                    action = new ActionSet(prdAction);
                    break;
            }
            //actions.add(new Action(action));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public List<Action> getActions() {
        return actions;
    }
}
