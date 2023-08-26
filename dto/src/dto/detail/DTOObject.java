package dto.detail;

import java.util.Map;

public abstract class DTOObject{

    protected final String name;

    public DTOObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Map<String, String> getFieldValueMap();

    @Override
    public String toString() {
        return name;
    }
}
