package engine;

import dto.detail.DTOEntity;
import dto.detail.DTOProperty;
import dto.detail.DTOEnvironmentVariable;
import dto.simulation.*;
import javafx.util.Pair;

import java.util.Collection;

public interface EngineInterface {

    void loadXml(String filepath);
    Collection<DTOEntityPopulation> getDetailsByEntityCount(int id);
    Collection<DTOEntity> getPastEntities(int id);
    Collection<DTOProperty> getPastEntityProperties(int id, String name);
    DTOSimulationHistogram getValuesForPropertyHistogram(int id, String name);
    Collection<DTOEnvironmentVariable> getEnvironmentDefinitions() throws NullPointerException;
    Collection<DTOEnvironmentVariable> getEnvironmentValues() throws NullPointerException;
    void setEnvironmentValues(Collection<Pair<String, Object>> envValues) throws NullPointerException;
    DTOSimulationResult runSimulation() throws NullPointerException;
    Collection<DTOSimulation> getPastSimulations();
    DTOSimulationDetails getSimulationDetails() throws NullPointerException;
    void saveToFile(String filepath);
    void loadFromFile(String filepath);

}
