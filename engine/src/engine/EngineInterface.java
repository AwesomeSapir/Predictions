package engine;

import dto.detail.*;
import dto.simulation.*;
import exception.FatalException;
import exception.SimulationMissingException;
import exception.XMLConfigException;
import exception.runtime.IllegalActionException;
import exception.runtime.IllegalUserActionException;
import exception.runtime.IncompatibleTypesException;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;

public interface EngineInterface {

    void loadXml(String filepath) throws FatalException, XMLConfigException, IncompatibleTypesException, IllegalActionException;
    Map<String, DTOEntityPopulation> getEntityPopulations(int id);
    Collection<DTOEntity> getEntities(int id);
    Collection<DTOProperty> getPastEntityProperties(int id, String name) throws IllegalActionException;
    DTOSimulationHistogram getValuesForPropertyHistogram(int id, String propertyName, String entityName) throws IllegalActionException;
    Collection<DTOEnvironmentVariable> getEnvironmentDefinitions() throws SimulationMissingException;
    Collection<DTOEnvironmentVariable> getEnvironmentValues() throws SimulationMissingException;
    void setEnvironmentValues(Collection<Pair<String, Object>> envValues) throws SimulationMissingException;
    DTOSimulation runSimulation() throws FatalException, XMLConfigException, IncompatibleTypesException, IllegalUserActionException, IllegalActionException, SimulationMissingException;

    DTOSimulationResult getSimulationResult(int id);

    Collection<DTOSimulation> getPastSimulations();

    DTOGrid getGrid(int id);

    DTOSimulationDetails getSimulationDetails() throws SimulationMissingException;
    void saveToFile(String filepath);
    void loadFromFile(String filepath);
    int getNextId();
    DTOStatus getSimulationStatus(int id);
    DTOTermination getSimulationTermination(int id);
    void pauseSimulation(int id) throws IllegalUserActionException;

    void tickSimulation(int id);

    void resumeSimulation(int id) throws IllegalUserActionException;

    DTOSpace getSimulationSpace(int id);

    void stopSimulation(int id) throws IllegalUserActionException;

    void setEntityPopulations(Collection<Pair<String, Integer>> entityPopulations) throws IllegalActionException, SimulationMissingException;

    Collection<Double> getTicksOfSameValueOfPropertyInstances(int id, String propertyName, String entityName) throws IllegalActionException;

    Collection<Double> getConsistencyOfProperty(int id, String propertyName, String entityName) throws IllegalActionException;

    DTOQueueDetails getQueueDetails();

    void shutdown();
}
