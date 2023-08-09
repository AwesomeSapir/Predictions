package world;

import world.instance.entity.EntityInstance;
import world.instance.environment.ActiveEnvironment;
import world.instance.property.PropertyInstance;

import java.util.Collection;

public interface Context {

    Collection<EntityInstance> getPrimaryEntityInstances();

    void removeEntity(EntityInstance entityInstance);

    PropertyInstance getEnvironmentPropertyInstance(String name);

}
