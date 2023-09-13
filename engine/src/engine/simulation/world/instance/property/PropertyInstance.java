package engine.simulation.world.instance.property;

import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.definition.property.PropertyType;

import java.io.Serializable;

public class PropertyInstance implements Serializable {

    private final PropertyDefinition propertyDefinition;
    private Object value;
    private int ticksOfSameValue = 0;

    public PropertyInstance(PropertyDefinition propertyDefinition){
        this.propertyDefinition = propertyDefinition;
        this.value = propertyDefinition.generateValue();
    }

    public void updateTicksOfSameValue() {
        ticksOfSameValue++;
    }

    public int getTicksOfSameValue() {
        return ticksOfSameValue;
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
        ticksOfSameValue = 0;
    }
}
