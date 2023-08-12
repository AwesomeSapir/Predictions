import javafx.util.Pair;

import java.util.Collection;
import java.util.List;

public class Engine implements EngineInterface{

    private List<Simulation> simulations;

    @Override
    public Object loadXml(String filepath) {
        return null;
    }

    @Override
    public Object getSimulationDetails() {
        return null;
    }

    @Override
    public Object getEnvironmentDefinitions() {
        return null;
    }

    @Override
    public Object setEnvironmentValues(Collection<Pair<String, Object>> envValues) {
        return null;
    }

    @Override
    public Object runSimulation() {
        return null;
    }

    @Override
    public Object getSimulations() {
        return null;
    }
}
