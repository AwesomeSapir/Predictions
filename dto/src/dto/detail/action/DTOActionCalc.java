package dto.detail.action;

import java.util.Map;

public class DTOActionCalc extends DTOAction{

    private final String resultPropertyName;
    private final String arg1;
    private final String arg2;

    public DTOActionCalc(String type, String entityName, String resultPropertyName, String arg1, String arg2) {
        super(type, entityName);
        this.resultPropertyName = resultPropertyName;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public Map<String, String> getFieldValueMap() {
        Map<String, String> fieldValues = super.getFieldValueMap();
        fieldValues.put("Result Property Name", resultPropertyName);
        fieldValues.put("Argument 1", arg1);
        fieldValues.put("Argument 2", arg2);
        return fieldValues;
    }

    public String getResultPropertyName() {
        return resultPropertyName;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }
}
