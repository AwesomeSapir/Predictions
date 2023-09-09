package dto.detail.action;

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
