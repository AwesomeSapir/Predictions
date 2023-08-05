package world.type;

import com.sun.org.apache.xpath.internal.operations.Bool;
import engine.prd.PRDValue;

import java.util.Objects;

public class Value {

    protected Range range;
    protected ValueType type;
    protected Object value;

    public Value(String valueString, ValueType type, Range range){
        this.type = type;
        this.range = range;
        switch (type){
            case Decimal:
                value = Integer.parseInt(valueString);
                break;
            case Float:
                value = Double.parseDouble(valueString);
                break;
            case Boolean:
                value = Boolean.parseBoolean(valueString);
                break;
            case String:
                value = valueString;
                break;
        }
    }

    public Value(PRDValue prdObject, ValueType type, Range range) {
        this.type = type;
        this.range = range;
        boolean randomInit = prdObject.isRandomInitialize();
        if(randomInit){
            value = 0; //TODO Random
        } else {
            switch (type){
                case Decimal:
                    value = Integer.parseInt(prdObject.getInit());
                    break;
                case Float:
                    value = Double.parseDouble(prdObject.getInit());
                    break;
                case Boolean:
                    value = Boolean.parseBoolean(prdObject.getInit());
                    break;
                case String:
                    value = prdObject.getInit();
                    break;
            }
        }
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return Objects.equals(value, value1.value);
    }

    public boolean biggerThan(Object o) {
        if (this == o) return false;
        if (o == null || getClass() != o.getClass()) return false;
        Double value1 = (Double) this.getValue();
        Double value2 = (Double) ((Value) o).getValue();
        return value1 > value2;
    }

    public boolean lessThan(Object o) {
        if (this == o) return false;
        if (o == null || getClass() != o.getClass()) return false;
        Double value1 = (Double) this.getValue();
        Double value2 = (Double) ((Value) o).getValue();
        return value1 < value2;
    }
}
