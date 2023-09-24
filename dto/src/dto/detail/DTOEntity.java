package dto.detail;

import java.util.Collection;

public class DTOEntity extends DTOObject {
    private final Collection<DTOProperty> properties;

    public DTOEntity(String name, Collection<DTOProperty> properties) {
        super(name);
        this.properties = properties;
    }

    public Collection<DTOProperty> getProperties() {
        return properties;
    }
}
