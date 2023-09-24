package engine.simulation.world.definition.entity;

import engine.simulation.world.definition.property.PropertyDefinition;

import java.io.Serializable;
import java.util.Map;

public class EntityDefinition implements Serializable {

    private final String name;
    private final Map<String, PropertyDefinition> properties;

    public EntityDefinition(String name, Map<String, PropertyDefinition> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public Map<String, PropertyDefinition> getProperties() {
        return properties;
    }
}
