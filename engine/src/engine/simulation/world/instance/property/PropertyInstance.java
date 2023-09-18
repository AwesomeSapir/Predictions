package engine.simulation.world.instance.property;

import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.ValueType;

import java.io.Serializable;

public class PropertyInstance implements Serializable {

    private final PropertyDefinition propertyDefinition;
    private Object value;
    private int ticksSinceChange = 0;
    private int tickSum = 0;
    private int changeCounter = 0;

    public PropertyInstance(PropertyDefinition propertyDefinition){
        this.propertyDefinition = propertyDefinition;
        this.value = propertyDefinition.generateValue();
    }

    public void copy(PropertyInstance old){
        this.value = old.value;
        this.changeCounter = old.changeCounter;
        this.tickSum = old.tickSum;
        this.ticksSinceChange = old.ticksSinceChange;
    }

    public void incrementTicksOfSameValue() {
        ticksSinceChange++;
    }

    public int getTicksSinceChange() {
        return ticksSinceChange;
    }

    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if(propertyDefinition.getType() == ValueType.DECIMAL || propertyDefinition.getType() == ValueType.FLOAT){
            if(propertyDefinition.getRange() != null && !propertyDefinition.getRange().isInRange(Double.parseDouble(value.toString()))){
                return;
            }
        }
        if(this.value != value) {
            this.value = value;
            tickSum += ticksSinceChange;
            changeCounter++;
            ticksSinceChange = 0;
        }
    }

    public double getConsistency(){
        if(changeCounter == 0){
            return ticksSinceChange;
        } else {
            return (double)tickSum / (double)changeCounter;
        }
    }
}
