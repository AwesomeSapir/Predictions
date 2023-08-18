package engine.world.rule;

import engine.world.rule.action.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rule implements Serializable {

    protected final String name;
    protected final Activation activation;
    protected final List<Action> actions = new ArrayList<>();

    public Rule(String name, Activation activation, Collection<Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actions.addAll(actions);
    }

    public String getName() {
        return name;
    }

    public Activation getActivation() {
        return activation;
    }

    public List<Action> getActions() {
        return actions;
    }
}
