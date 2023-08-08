package world.instance.environment;

import engine.prd.PRDEnvProperty;
import engine.prd.PRDEvironment;
import world.definition.property.AbstractPropertyDefinition;
import world.definition.property.PropertyDefinition;
import world.definition.property.PropertyType;
import world.type.Range;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentManager {

    private final Map<String, PropertyDefinition> properties;

    public EnvironmentManager() {
        properties = new HashMap<>();
    }

    public EnvironmentManager(PRDEvironment prdObject) {
        properties = new HashMap<>();
        for (PRDEnvProperty prdProperty : prdObject.getPRDEnvProperty()) {
            PropertyType type = PropertyType.valueOf(prdProperty.getType().toUpperCase());
            Range range = new Range(prdProperty.getPRDRange());
            PropertyDefinition propertyDefinition = AbstractPropertyDefinition.createPropertyDefinitionByType(prdProperty.getPRDName(), type, null, range, true);
            properties.put(prdProperty.getPRDName(), propertyDefinition);
        }
    }

    public void addEnvironmentVariable(PropertyDefinition propertyDefinition) {
        properties.put(propertyDefinition.getName(), propertyDefinition);
    }

    public Collection<PropertyDefinition> getVariables() {
        return properties.values();
    }
}
