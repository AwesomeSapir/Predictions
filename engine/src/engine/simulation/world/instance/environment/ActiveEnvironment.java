package engine.simulation.world.instance.environment;

import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.instance.property.PropertyInstance;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ActiveEnvironment implements Serializable {
    private final Map<String, PropertyInstance> envVariables;

    public ActiveEnvironment() {
        envVariables = new HashMap<>();
    }

    public void initProperties(Collection<PropertyDefinition> propertyDefinitions){
        for (PropertyDefinition propertyDefinition : propertyDefinitions){
            addPropertyInstance(new PropertyInstance(propertyDefinition));
        }
    }

    public PropertyInstance getProperty(String name) throws UnsupportedOperationException{
        if(!envVariables.containsKey(name)) {
            throw new UnsupportedOperationException("Can't find env variable with name " + name);
        }
        return envVariables.get(name);
    }

    public void addPropertyInstance(PropertyInstance propertyInstance){
        envVariables.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }
}
