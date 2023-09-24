package engine.simulation.world;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.environment.EnvironmentManager;
import engine.simulation.world.rule.Rule;
import engine.simulation.world.termination.Termination;

import java.util.*;

public class WorldDefinition {

    protected final EnvironmentManager environmentManager;
    //protected final Map<String, EntityDefinition> entityDefinitions = new LinkedHashMap<>();
    protected final List<EntityDefinition> entityDefinitions = new ArrayList<>();
    protected final Map<String, Rule> rules;
    protected final Termination termination;
    protected final int rows, cols;
    protected final int threadCount;

    public WorldDefinition(EnvironmentManager environmentManager, Collection<EntityDefinition> entityDefinitions, Map<String, Rule> rules, Termination termination, int rows, int cols, int threadCount) {
        this.environmentManager = environmentManager;
        this.entityDefinitions.addAll(entityDefinitions);
        this.rules = rules;
        this.termination = termination;
        this.rows = rows;
        this.cols = cols;
        this.threadCount = threadCount;
    }

    public EnvironmentManager getEnvironmentManager() {
        return environmentManager;
    }

    public List<EntityDefinition> getEntityDefinitions() {
        return entityDefinitions;
    }

    public Map<String, Rule> getRules() {
        return rules;
    }

    public Termination getTermination() {
        return termination;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
