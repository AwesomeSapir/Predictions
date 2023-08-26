package dto.detail.action;

import dto.detail.DTOObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class DTOAction extends DTOObject {

    protected final String entityName;

    public DTOAction(String type, String entityName) {
        super(type);
        this.entityName = entityName;
    }

    @Override
    public Map<String, String> getFieldValueMap() {
        Map<String, String> fieldValues = new LinkedHashMap<>();
        fieldValues.put("Type", name);
        fieldValues.put("Entity Name", entityName);
        return fieldValues;
    }

    public String getEntityName() {
        return entityName;
    }
}
