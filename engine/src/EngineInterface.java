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
    Collection<DTOEnvironmentVariable> getEnvironmentDefinitions();
    void setEnvironmentValues(Collection<Pair<String, Object>> envValues);
    DTOSimulationResult runSimulation();
    Collection<DTOSimulation> getPastSimulations();
    DTOSimulationDetails getSimulationDetails();

}
