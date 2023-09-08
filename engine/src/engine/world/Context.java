package engine.world;

import engine.world.definition.entity.EntityDefinition;
import engine.world.instance.entity.EntityInstance;
import engine.world.instance.property.PropertyInstance;
import engine.world.space.SpaceManager;

import java.util.Collection;

public interface Context {

    void removeEntity(EntityInstance entityInstance);

    public EntityDefinition getEntityDefinition(String name);

    public Collection<EntityInstance> getEntityInstances(EntityDefinition entityDefinition);

    PropertyInstance getEnvironmentPropertyInstance(String name);

    Collection<EntityInstance> getEntityInstances(EntityDefinition entityDefinition, int count);

    SpaceManager getSpaceManager();

}
