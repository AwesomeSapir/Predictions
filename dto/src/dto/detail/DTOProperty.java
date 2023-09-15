package dto.detail;

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

    @Override
    public String getName() {
        return super.getName();
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
}
