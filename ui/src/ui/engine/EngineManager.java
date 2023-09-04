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
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class EngineManager {

    public final EngineInterface engine; //TODO make private
    private final ObservableList<Simulation> simulations = FXCollections.observableArrayList();

    private BooleanProperty isSimulationLoaded = new SimpleBooleanProperty(false);
    private StringProperty simulationPath = new SimpleStringProperty();

    public EngineManager() {
        engine = new Engine();
    }

    private final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

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
        Task<DTOSimulationResult> task = new Task<DTOSimulationResult>() {
            @Override
            protected DTOSimulationResult call() throws Exception {
                return engine.runSimulation();
            }
        };
        task.setOnSucceeded(event -> simulations.get(task.getValue().getId()-1).setResult(task.getValue()));
        simulations.add(new Simulation(engine.getNextId(), engine.getSimulationTermination()));
        threadPool.execute(task);
    }

    public void resumeSimulation(int id){
        Task<DTOSimulationResult> task = new Task<DTOSimulationResult>() {
            @Override
            protected DTOSimulationResult call() throws Exception {
                return engine.resumeSimulation(id);
            }
        };
        task.setOnSucceeded(event -> simulations.get(task.getValue().getId()-1).setResult(task.getValue()));
        threadPool.execute(task);
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

    public ObservableList<Simulation> getSimulations() {
        return simulations;
    }

    public DTOSimulationDetails getSimulationDetails(){
        return engine.getSimulationDetails();
    }

    public Collection<DTOEnvironmentVariable> getEnvironmentDefinitions(){
        return engine.getEnvironmentDefinitions();
    }
}
