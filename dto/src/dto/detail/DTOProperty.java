package dto.detail;

public class DTOProperty {
    private final String name;
    private final String type;
    private final DTORange range;
    private final boolean isRandomInit;

    public DTOProperty(String name, String type, DTORange range, boolean isRandomInit) {
        this.name = name;
        this.type = type;
        this.range = range;
        this.isRandomInit = isRandomInit;
    }

    public String getName() {
        return name;
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
