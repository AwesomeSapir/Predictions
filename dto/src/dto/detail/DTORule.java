package dto.detail;

import java.util.Collection;

public class DTORule {

    private final String name;
    private final int ticks;
    private final double probability;
    private final Collection<String> actions;

    public DTORule(String name, int ticks, double probability, Collection<String> actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    public Collection<String> getActions() {
        return actions;
    }

    public int getActionsAmount() {
        return actions.size();
    }
}
