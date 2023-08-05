package world;

import engine.prd.PRDEntity;
import world.property.Property;

import java.util.Map;

public class Entity {
    
    protected String name;
    protected int population;
    Map<String, Property> propertyMap;

    public Entity(PRDEntity prdObject) {
        name = prdObject.getName();
        population = prdObject.getPRDPopulation();
        //TODO what about property value??
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Map<String, Property> getPropertyMap() {
        return propertyMap;
    }

    //TODO maybe make private
    public void setPropertyValueMap(Map<String, Property> propertyMap) {
        this.propertyMap = propertyMap;
    }
}
