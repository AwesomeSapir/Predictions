package world.definition.entity;

import world.definition.property.PropertyDefinition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityDefinition implements Serializable {

    private final String name;
    private final int population;
    private final Map<String, PropertyDefinition> properties;

    public EntityDefinition(String name, int population, Map<String, PropertyDefinition> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }

    public EntityDefinition(String name, int population) {
        this.name = name;
        this.population = population;
        this.properties = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public Map<String, PropertyDefinition> getProperties() {
        return properties;
    }
}
