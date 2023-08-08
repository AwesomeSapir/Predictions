package world.instance.property;

import world.definition.property.PropertyDefinition;

public class PropertyInstance {

    private PropertyDefinition propertyDefinition;
    private Object value;

    public PropertyInstance(PropertyDefinition propertyDefinition, Object value) {
        this.propertyDefinition = propertyDefinition;
        this.value = value;
    }

    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        //TODO maybe check if valid
    }
}
