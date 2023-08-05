package world.rule;

import engine.prd.PRDAction;
import engine.prd.PRDRule;
import world.rule.action.Action;
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
            actions.add(Action.createActionFromPRD(prdAction));
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
