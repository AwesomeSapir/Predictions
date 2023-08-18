package engine.world.definition.entity;

import engine.world.definition.property.PropertyDefinition;

import java.io.Serializable;
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
