package world.instance.environment;

import world.definition.property.PropertyDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentManager {

    private final Map<String, PropertyDefinition> properties;

    public EnvironmentManager() {
        properties = new HashMap<>();
    }

    public void addEnvironmentVariable(PropertyDefinition propertyDefinition){
        properties.put(propertyDefinition.getName(), propertyDefinition);
    }

    public Collection<PropertyDefinition> getVariables(){
        return properties.values();
    }
}
