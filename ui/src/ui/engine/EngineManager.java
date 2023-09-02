package ui.engine;

import dto.detail.DTOEnvironmentVariable;
import dto.simulation.DTOSimulationDetails;
import dto.simulation.DTOSimulationResult;
import engine.Engine;
import engine.EngineInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.File;
import java.util.Collection;

public class EngineManager {

    public final EngineInterface engine;
    private final ObservableList<DTOSimulationResult> simulations = FXCollections.observableArrayList();

    private BooleanProperty isSimulationLoaded = new SimpleBooleanProperty(false);
    private StringProperty simulationPath = new SimpleStringProperty();

    public EngineManager() {
        engine = new Engine();
    }

    public void loadSimulation(File file){
        engine.loadXml(file.getAbsolutePath());
        isSimulationLoaded.set(false);
        isSimulationLoaded.set(true);
        simulationPath.set(file.getAbsolutePath());
    }

    public void setEnvironmentValues(Collection<Pair<String, Object>> values){
        engine.setEnvironmentValues(values);
    }

    public void runSimulation(){
        DTOSimulationResult result = engine.runSimulation();
        simulations.add(result);
    }

    /*
     * Getters
     */

    public boolean isSimulationLoaded() {
        return isSimulationLoaded.get();
    }

    public BooleanProperty isSimulationLoadedProperty() {
        return isSimulationLoaded;
    }

    public String getSimulationPath() {
        return simulationPath.get();
    }

    public StringProperty simulationPathProperty() {
        return simulationPath;
    }

    public ObservableList<DTOSimulationResult> getSimulations() {
        return simulations;
    }

    public DTOSimulationDetails getSimulationDetails(){
        return engine.getSimulationDetails();
    }

    public Collection<DTOEnvironmentVariable> getEnvironmentDefinitions(){
        return engine.getEnvironmentDefinitions();
    }
}
