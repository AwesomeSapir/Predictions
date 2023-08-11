package world;

import world.definition.entity.EntityDefinition;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

import java.util.Collection;

public interface Context {

    Collection<EntityInstance> getPrimaryEntityInstances();

    void removeEntity(EntityInstance entityInstance);

    EntityDefinition getPrimaryEntityDefinition();

    PropertyInstance getEnvironmentPropertyInstance(String name);

}
