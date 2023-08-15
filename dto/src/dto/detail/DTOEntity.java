package dto.detail;

import java.util.Collection;

public class DTOEntity {

    private final String name;
    private final int population;
    private final Collection<DTOProperty> properties;

    public DTOEntity(String name, int population, Collection<DTOProperty> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public Collection<DTOProperty> getProperties() {
        return properties;
    }
}
