package engine.world;

import engine.world.instance.entity.EntityInstance;
import engine.world.instance.entity.EntityManager;
import engine.world.instance.environment.EnvironmentManager;
import engine.world.instance.property.PropertyInstance;
import engine.world.space.SpaceManager;

public interface Context {

    void removeEntity(EntityInstance entityInstance);

    PropertyInstance getEnvironmentPropertyInstance(String name);

    EntityManager getEntityManager();

    EnvironmentManager getEnvironmentManager();

    SpaceManager getSpaceManager();

}
