package engine.world;

import engine.world.definition.entity.EntityDefinition;
import engine.world.instance.entity.EntityInstance;
import engine.world.instance.property.PropertyInstance;
import engine.world.space.SpaceManager;

import java.util.Collection;

public interface Context {

    Collection<EntityInstance> getPrimaryEntityInstances();

    void removeEntity(EntityInstance entityInstance);

    EntityDefinition getPrimaryEntityDefinition();

    PropertyInstance getEnvironmentPropertyInstance(String name);

    SpaceManager getSpaceManager();

}
