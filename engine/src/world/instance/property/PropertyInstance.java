package world.instance.property;

import world.definition.property.PropertyDefinition;
import world.definition.property.PropertyType;

import java.io.Serializable;

public class PropertyInstance implements Serializable {

    private PropertyDefinition propertyDefinition;
    private Object value;

    public PropertyInstance(PropertyDefinition propertyDefinition, Object value) {
        this.propertyDefinition = propertyDefinition;
        this.value = value;
    }

    public PropertyInstance(PropertyDefinition propertyDefinition){
        this.propertyDefinition = propertyDefinition;
        this.value = propertyDefinition.generateValue();
    }

    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if(propertyDefinition.getType() == PropertyType.DECIMAL || propertyDefinition.getType() == PropertyType.FLOAT){
            if(propertyDefinition.getRange() != null && !propertyDefinition.getRange().isInRange(Double.parseDouble(value.toString()))){
                return;
            }
        }
        this.value = value;
    }
}
