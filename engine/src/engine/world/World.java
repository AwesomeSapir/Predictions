package engine.world;

import engine.world.definition.entity.EntityDefinition;
import engine.world.instance.entity.EntityInstance;
import engine.world.instance.entity.EntityManager;
import engine.world.instance.environment.ActiveEnvironment;
import engine.world.instance.environment.EnvironmentManager;
import engine.world.instance.property.PropertyInstance;
import engine.world.rule.Rule;
import engine.world.space.SpaceManager;
import engine.world.termination.Termination;

import java.io.Serializable;
import java.util.*;

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
    public EntityDefinition getEntityDefinition(String name) {
        return entityManager.getEntityDefinition(name);
    }

    public Collection<EntityDefinition> getEntityDefinitions(){
        return entityManager.getEntityDefinitions();
    }

    @Override
    public Collection<EntityInstance> getEntityInstances(EntityDefinition entityDefinition) {
        return entityManager.getEntityInstances(entityDefinition);
    }

    @Override
    public Collection<EntityInstance> getEntityInstances(EntityDefinition entityDefinition, int count) {
        List<EntityInstance> entityInstances = new ArrayList<>(entityManager.getEntityInstances(entityDefinition));
        List<EntityInstance> result = new ArrayList<>();
        count = Math.min(count, entityInstances.size());
        for (int i=0; i<count; i++){
            result.add(entityInstances.get(i));
        }
        return result;
    }

    public Collection<EntityInstance> getAllInstances(){
        List<EntityInstance> result = new ArrayList<>();
        for (EntityDefinition entityDefinition : entityManager.getEntityDefinitions()){
            result.addAll(getEntityInstances(entityDefinition));
        }
        return result;
    }

    public EnvironmentManager getEnvironmentManager() {
        return environmentManager;
    }

    @Override
    public SpaceManager getSpaceManager() {
        return spaceManager;
    }
}
