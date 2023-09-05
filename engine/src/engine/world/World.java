package engine.world;

import engine.world.definition.entity.EntityDefinition;
import engine.world.instance.entity.EntityInstance;
import engine.world.instance.environment.ActiveEnvironment;
import engine.world.instance.environment.EnvironmentManager;
import engine.world.instance.property.PropertyInstance;
import engine.world.rule.Rule;
import engine.world.space.SpaceManager;
import engine.world.termination.Termination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class World implements Context, Serializable {

    protected final EnvironmentManager environmentManager;
    protected final ActiveEnvironment activeEnvironment;
    protected final EntityDefinition primaryEntityDefinition;
    protected final List<EntityInstance> entityInstances = new ArrayList<>();
    protected final Map<String, Rule> rules;
    protected final Termination termination;
    protected final SpaceManager spaceManager;

    public World(EnvironmentManager environmentManager, ActiveEnvironment activeEnvironment, EntityDefinition primaryEntityDefinition, Collection<EntityInstance> entityInstances, Map<String, Rule> rules, Termination termination, SpaceManager spaceManager) {
        this.environmentManager = environmentManager;
        this.activeEnvironment = activeEnvironment;
        this.primaryEntityDefinition = primaryEntityDefinition;
        this.entityInstances.addAll(entityInstances);
        this.rules = rules;
        this.termination = termination;
        this.spaceManager = spaceManager;
    }

    public Map<String, Rule> getRules() {
        return rules;
    }

    public Termination getTermination() {
        return termination;
    }

    @Override
    public PropertyInstance getEnvironmentPropertyInstance(String name) {
        return activeEnvironment.getProperty(name);
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {
        spaceManager.removeEntity(entityInstance);
        entityInstances.remove(entityInstance);
    }

    @Override
    public EntityDefinition getPrimaryEntityDefinition() {
        return primaryEntityDefinition;
    }

    @Override
    public Collection<EntityInstance> getPrimaryEntityInstances() {
        return entityInstances;
    }

    public EnvironmentManager getEnvironmentManager() {
        return environmentManager;
    }

    @Override
    public SpaceManager getSpaceManager() {
        return spaceManager;
    }
}
