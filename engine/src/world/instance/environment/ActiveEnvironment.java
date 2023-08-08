package world.instance.environment;

import world.instance.property.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class ActiveEnvironment {

    private final Map<String, PropertyInstance> envVariables;

    public ActiveEnvironment() {
        envVariables = new HashMap<>();
    }

    public PropertyInstance getProperty(String name){
        if(!envVariables.containsKey(name)) {
            throw new IllegalArgumentException("Can't find env variable with name " + name);
        }
        return envVariables.get(name);
    }

    public void addPropertyInstance(PropertyInstance propertyInstance){
        envVariables.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }
}
