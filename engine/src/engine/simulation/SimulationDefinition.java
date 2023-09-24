package engine.simulation;

import engine.simulation.world.WorldDefinition;

public class SimulationDefinition {

    protected final String name;
    protected final long sleepMillis;
    protected final WorldDefinition worldDefinition;

    public SimulationDefinition(String name, WorldDefinition worldDefinition, long sleepMillis) {
        this.name = name;
        this.sleepMillis = sleepMillis;
        this.worldDefinition = worldDefinition;
    }

    public String getName() {
        return name;
    }

    public long getSleepMillis() {
        return sleepMillis;
    }

    public WorldDefinition getWorldDefinition() {
        return worldDefinition;
    }
}
