package engine.simulation.world;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.instance.entity.EntityManager;
import engine.simulation.world.instance.environment.ActiveEnvironment;
import engine.simulation.world.instance.environment.EnvironmentManager;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.rule.Rule;
import engine.simulation.world.space.SpaceManager;
import exception.runtime.IllegalActionException;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class World implements Context, Serializable {

    protected final WorldDefinition worldDefinition;
    protected final ActiveEnvironment activeEnvironment;
    protected final EntityManager entityManager;
    protected final SpaceManager spaceManager;

    protected final Map<String, Integer> entityPopulations = new LinkedHashMap<>();

    public World(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
        //TODO init here or get in constructor

        activeEnvironment = worldDefinition.environmentManager.createActiveEnvironment();
        activeEnvironment.initProperties(worldDefinition.environmentManager.getVariables());

        entityManager = new EntityManager(worldDefinition.entityDefinitions);

        spaceManager = new SpaceManager(worldDefinition.rows, worldDefinition.cols);
    }

    public WorldDefinition getWorldDefinition() {
        return worldDefinition;
    }

    public Map<String, Rule> getRules() {
        return worldDefinition.rules;
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
        return worldDefinition.environmentManager;
    }

    @Override
    public SpaceManager getSpaceManager() {
        return spaceManager;
    }

    @Override
    public int getThreadCount() {
        return worldDefinition.threadCount;
    }

    public void setEntityPopulation(String name, int population) throws IllegalActionException {
        EntityDefinition entityDefinition = getEntityManager().getEntityDefinition(name);
        entityPopulations.put(entityDefinition.getName(), population);
    }

    public Map<String, Integer> getEntityPopulations() {
        return entityPopulations;
    }
}
