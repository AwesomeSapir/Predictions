package dto.detail;

import java.util.LinkedHashMap;
import java.util.Map;

public class DTOEnvironmentVariable extends DTOObject{
    private final String type;
    private final DTORange range;
    private final Object value;

    public DTOEnvironmentVariable(String name, String type, DTORange range) {
        super(name);
        this.type = type;
        this.range = range;
        this.value = null;
    }

    public DTOEnvironmentVariable(String name, Object value) {
        super(name);
        this.type = null;
        this.range = null;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public DTORange getRange() {
        return range;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Map<String, String> getFieldValueMap() {
        Map<String, String> fieldValues = new LinkedHashMap<>();
        fieldValues.put("Name", name);
        fieldValues.put("Type", type);
        if(range != null) {
            fieldValues.put("Range", range.getFrom() + " - " + range.getTo());
        }
        return fieldValues;
    }
}
