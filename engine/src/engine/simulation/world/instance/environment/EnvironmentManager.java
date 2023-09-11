package engine.simulation.world.instance.environment;

import engine.simulation.world.definition.property.PropertyDefinition;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class EnvironmentManager implements Serializable {

    private final Map<String, PropertyDefinition> properties;

    public EnvironmentManager(Map<String, PropertyDefinition> properties) {
        this.properties = properties;
    }

    public void addEnvironmentVariable(PropertyDefinition propertyDefinition) {
        properties.put(propertyDefinition.getName(), propertyDefinition);
    }

    public Collection<PropertyDefinition> getVariables() {
        return properties.values();
    }

    public ActiveEnvironment createActiveEnvironment() {
        return new ActiveEnvironment();
    }
}
