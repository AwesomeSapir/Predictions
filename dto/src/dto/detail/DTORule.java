package dto.detail;

import dto.detail.action.DTOAction;

import java.util.Collection;

public class DTORule extends DTOObject{
    private final int ticks;
    private final double probability;
    private final Collection<DTOAction> actions;

    public DTORule(String name, int ticks, double probability, Collection<DTOAction> actions) {
        super(name);
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    public Collection<DTOAction> getActions() {
        return actions;
    }

    public int getActionsAmount() {
        return actions.size();
    }
}
