package world.definition.entity;

import engine.prd.PRDEntity;
import engine.prd.PRDProperty;
import world.definition.property.AbstractPropertyDefinition;
import world.definition.property.PropertyDefinition;
import world.definition.property.PropertyType;
import world.type.Range;

import java.util.HashMap;
import java.util.Map;

public class EntityDefinition {

    private final String name;
    private final int population;
    private final Map<String, PropertyDefinition> properties;

    public EntityDefinition(PRDEntity prdObject) {
        name = prdObject.getName();
        population = prdObject.getPRDPopulation();
        properties = new HashMap<>();
        for (PRDProperty prdProperty : prdObject.getPRDProperties().getPRDProperty()) {
            String name = prdProperty.getPRDName();
            PropertyType type = PropertyType.valueOf(prdProperty.getType().toUpperCase());
            String init = prdProperty.getPRDValue().getInit();
            Range range = new Range(prdProperty.getPRDRange());
            boolean isRandomInit = prdProperty.getPRDValue().isRandomInitialize();
            PropertyDefinition propertyDefinition = AbstractPropertyDefinition.createPropertyDefinitionByType(name, type, init, range, isRandomInit);
            properties.put(prdProperty.getPRDName(), propertyDefinition);
        }
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
