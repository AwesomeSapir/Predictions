package engine.world.instance.entity;

import engine.world.definition.entity.EntityDefinition;
import engine.world.definition.property.PropertyDefinition;
import engine.world.instance.property.PropertyInstance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityInstance implements Serializable {

    private int x,y;
    private final EntityDefinition entityDefinition;
    private final Map<String, PropertyInstance> properties;

    public EntityInstance(EntityDefinition entityDefinition) {
        this.entityDefinition = entityDefinition;
        properties = new HashMap<>();
    }

    public void initProperties(){
        for (PropertyDefinition propertyDefinition : entityDefinition.getProperties().values()){
            addPropertyInstance(new PropertyInstance(propertyDefinition));
        }
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

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
