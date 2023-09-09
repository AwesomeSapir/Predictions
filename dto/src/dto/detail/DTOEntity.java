package dto.detail;

import java.util.Collection;

public class DTOEntity extends DTOObject {
    private final int population;
    private final Collection<DTOProperty> properties;

    public DTOEntity(String name, int population, Collection<DTOProperty> properties) {
        super(name);
        this.population = population;
        this.properties = properties;
    }

    public int getPopulation() {
        return population;
    }

    public Collection<DTOProperty> getProperties() {
        return properties;
    }
}
