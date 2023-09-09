package dto.detail.action;

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
}
