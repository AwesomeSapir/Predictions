package engine.world;

import engine.world.instance.entity.EntityInstance;
import engine.world.instance.entity.EntityManager;
import engine.world.instance.environment.ActiveEnvironment;
import engine.world.instance.environment.EnvironmentManager;
import engine.world.instance.property.PropertyInstance;
import engine.world.rule.Rule;
import engine.world.space.SpaceManager;
import engine.world.termination.Termination;

import java.io.Serializable;
import java.util.Map;

public class World implements Context, Serializable {

    protected final EnvironmentManager environmentManager;
    protected final ActiveEnvironment activeEnvironment;
    protected final EntityManager entityManager;
    protected final Map<String, Rule> rules;
    protected final Termination termination;
    protected final SpaceManager spaceManager;

    public World(EnvironmentManager environmentManager, ActiveEnvironment activeEnvironment, EntityManager entityManager, Map<String, Rule> rules, Termination termination, SpaceManager spaceManager) {
        this.environmentManager = environmentManager;
        this.activeEnvironment = activeEnvironment;
        this.entityManager = entityManager;
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
        entityManager.removeEntity(entityInstance);
    }

    @Override
    public EntityManager getEntityManager(){
        return entityManager;
    }

    @Override
    public EnvironmentManager getEnvironmentManager() {
        return environmentManager;
    }

    @Override
    public SpaceManager getSpaceManager() {
        return spaceManager;
    }
}
