package world.property;

import engine.prd.PRDProperty;
import world.type.Range;
import world.type.Value;
import world.type.ValueType;

//Unique for every entity type
public class Property {

    protected String name;
    private Value value;

    public Property(PRDProperty prdObject) {
        name = prdObject.getPRDName();
        Range range = new Range(prdObject.getPRDRange());
        ValueType type = Enum.valueOf(ValueType.class, prdObject.getType());
        value = new Value(prdObject.getPRDValue(), type, range);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Range getRange() {
        return value.getRange();
    }

    public void setRange(Range range) {
        this.value.setRange(range);
    }

    public Object getValue() {
        return value.getValue();
    }

    public void setValue(Object value) {
        this.value.setValue(value);
    }

    public ValueType getType() {
        return value.getType();
    }

    public void setType(ValueType type) {
        this.value.setType(type);
    }

    public Value getPropertyValue() {
        return value;
    }

    public void setPropertyValue(Value value) {
        this.value = value
        ;
    }
}
