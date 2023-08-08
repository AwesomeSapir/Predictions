package world.instance.entity;

import world.definition.entity.EntityDefinition;
import world.instance.property.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EntityInstance {

    private final EntityDefinition entityDefinition;
    private final int id;
    private Map<String, PropertyInstance> properties;

    public EntityInstance(EntityDefinition entityDefinition, int id) {
        this.entityDefinition = entityDefinition;
        this.id = id;
        properties = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public PropertyInstance getPropertyByName(String name){
        if(!properties.containsKey(name)){
            throw new IllegalArgumentException("For entity of type " + entityDefinition.getName() + " has no property named " + name);
        }
        return properties.get(name);
    }

    public void addPropertyInstance(PropertyInstance propertyInstance){
        properties.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }
}
