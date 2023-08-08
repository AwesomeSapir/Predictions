package world.property;

import engine.prd.PRDEnvProperty;
import world.type.Range;
import world.type.Value;
import world.type.ValueType;

public class EnvProperty {
    protected String name;
    protected Range range;
    protected ValueType type;
    protected Value value;

    public EnvProperty(PRDEnvProperty prdObject) {
        name = prdObject.getPRDName();
        range = new Range(prdObject.getPRDRange());
        type = Enum.valueOf(ValueType.class, prdObject.getType());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public boolean isInitialized(){
        return (value != null);
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
