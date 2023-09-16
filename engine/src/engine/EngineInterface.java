package engine;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import dto.detail.DTOProperty;
import dto.detail.DTOTermination;
import dto.simulation.*;
import javafx.util.Pair;

import java.util.Collection;

public interface EngineInterface {

    void loadXml(String filepath);
    Collection<DTOEntityPopulation> getDetailsByEntityCount(int id);
    Collection<DTOEntity> getPastEntities(int id);
    Collection<DTOProperty> getPastEntityProperties(int id, String name);
    DTOSimulationHistogram getValuesForPropertyHistogram(int id, String propertyName, String entityName);
    Collection<DTOEnvironmentVariable> getEnvironmentDefinitions() throws NullPointerException;
    Collection<DTOEnvironmentVariable> getEnvironmentValues() throws NullPointerException;
    void setEnvironmentValues(Collection<Pair<String, Object>> envValues) throws NullPointerException;
    DTOSimulation runSimulation() throws NullPointerException;

    DTOSimulationResult getSimulationResult(int id);

    Collection<DTOSimulation> getPastSimulations();
    DTOSimulationDetails getSimulationDetails() throws NullPointerException;
    void saveToFile(String filepath);
    void loadFromFile(String filepath);
    int getNextId();
    DTOStatus getSimulationStatus(int id);
    DTOTermination getSimulationTermination(int id);
    void pauseSimulation(int id);

    void tickSimulation(int id);

    void resumeSimulation(int id);

    DTOSpace getSimulationSpace(int id);

    void stopSimulation(int id);

    void setEntityPopulations(Collection<Pair<String, Integer>> entityPopulations);

    Collection<Double> getTicksOfSameValueOfPropertyInstances(int id, String propertyName, String entityName);
}
