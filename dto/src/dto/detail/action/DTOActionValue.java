package dto.detail.action;

import java.util.Map;

public class DTOActionValue extends DTOAction{

    protected final String propertyName;
    protected final String value;

    public DTOActionValue(String type, String entityName, String propertyName, String value) {
        super(type, entityName);
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Map<String, String> getFieldValueMap() {
        Map<String, String> fieldValues = super.getFieldValueMap();
        fieldValues.put("Property Name", propertyName);
        fieldValues.put("Value", value);
        return fieldValues;
    }
}
