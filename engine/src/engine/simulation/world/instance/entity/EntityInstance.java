package engine.simulation.world.instance.entity;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.space.Point;
import exception.runtime.IllegalActionException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityInstance implements Serializable {

    private Point point;
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

    public Collection<PropertyInstance> getPropertyInstances(){
        return properties.values();
    }

    public PropertyInstance getPropertyByName(String name) throws IllegalActionException {
        if(!properties.containsKey(name)){
            throw new IllegalActionException("For entity of type " + entityDefinition.getName() + " has no property named " + name);
        }
        return properties.get(name);
    }

    public PropertyInstance getPropertyIfExists(String name){
        if(!properties.containsKey(name)){
            return null;
        }
        return properties.get(name);
    }

    public void addPropertyInstance(PropertyInstance propertyInstance){
        properties.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public void setPoint(Point point){
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public int getX() {
        return point.x();
    }

    public int getY() {
        return point.y();
    }
}
