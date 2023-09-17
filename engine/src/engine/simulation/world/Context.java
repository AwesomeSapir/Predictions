package engine.simulation.world;

import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.instance.environment.EnvironmentManager;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.instance.entity.EntityManager;
import engine.simulation.world.space.SpaceManager;

public interface Context {

    void removeEntity(EntityInstance entityInstance);

    PropertyInstance getEnvironmentPropertyInstance(String name);

    EntityManager getEntityManager();

    EnvironmentManager getEnvironmentManager();

    SpaceManager getSpaceManager();

    int getThreadCount();
}
