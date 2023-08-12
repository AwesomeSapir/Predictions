package world.rule;

import world.rule.action.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rule {

    protected String name;
    protected Activation activation;
    protected List<Action> actions = new ArrayList<>();

    public Rule(String name, Activation activation, Collection<Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actions.addAll(actions);
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
