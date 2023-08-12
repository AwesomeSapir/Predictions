import javafx.util.Pair;

import java.util.Collection;

public interface SimulationInterface {

    Object loadXml(String filepath);
    Object getSimulationDetails();
    Object getEnvironmentDefinitions();
    Object setEnvironmentValues(Collection<Pair<String, Object>> envValues);
    Object runSimulation();
    Object getSimulations();

}
