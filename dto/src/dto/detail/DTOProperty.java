package dto.detail;

import java.util.LinkedHashMap;
import java.util.Map;

public class DTOProperty extends DTOObject {
    private final String type;
    private final DTORange range;
    private final boolean isRandomInit;

    public DTOProperty(String name, String type, DTORange range, boolean isRandomInit) {
        super(name);
        this.type = type;
        this.range = range;
        this.isRandomInit = isRandomInit;
    }

    public String getType() {
        return type;
    }

    public DTORange getRange() {
        return range;
    }

    public boolean isRandomInit() {
        return isRandomInit;
    }

    @Override
    public Map<String, String> getFieldValueMap() {
        Map<String, String> fieldValues = new LinkedHashMap<>();
        fieldValues.put("Name", name);
        fieldValues.put("Type", type);
        if(range != null) {
            fieldValues.put("Range", range.getFrom() + " - " + range.getTo());
        }
        fieldValues.put("Random", String.valueOf(isRandomInit));
        return fieldValues;
    }
}
