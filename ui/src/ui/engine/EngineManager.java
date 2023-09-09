package ui.engine;

import dto.detail.DTOEnvironmentVariable;
import dto.simulation.DTOSimulation;
import dto.simulation.DTOSimulationDetails;
import dto.simulation.DTOStatus;
import engine.Engine;
import engine.EngineInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class EngineManager {

    public final EngineInterface engine; //TODO make private
    private final ObservableMap<Integer, Simulation> simulations = FXCollections.observableHashMap();
    private final ObservableList<Simulation> simulationsList = FXCollections.observableArrayList();
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
        Task<DTOSimulation> task = new Task<DTOSimulation>() {
            @Override
            protected DTOSimulation call() throws Exception {
                return engine.runSimulation();
            }
        };
        task.setOnSucceeded(event -> {
            int id = task.getValue().getId();
            simulations.get(id).setStatus(Status.valueOf(engine.getSimulationStatus(id).getStatus()));
        });
        Simulation simulation =  new Simulation(engine.getNextId(), engine.getSimulationTermination());
        simulations.put(simulation.getId(), simulation);
        simulationsList.add(simulation);

        threadPool.execute(task);
    }

    public void resumeSimulation(int id){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                engine.resumeSimulation(id);
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            simulations.get(id).setStatus(Status.valueOf(engine.getSimulationStatus(id).getStatus()));
        });
        threadPool.execute(task);
        simulations.get(id).setStatus(Status.RUNNING);
    }

    public void pauseSimulation(int id){
        engine.pauseSimulation(id);
        simulations.get(id).setStatus(Status.PAUSED);
    }

    public void stopSimulation(int id){
        engine.stopSimulation(id);
        simulations.get(id).setStatus(Status.STOPPED);
    }

    public void updateSimulationProgress(Simulation simulation) {
        DTOStatus status = engine.getSimulationStatus(simulation.getId());
        simulation.getProgressSeconds().setValue(status.getMillis() / 1000.0);
        simulation.getProgressTicks().setValue(status.getTicks());
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

    public ObservableMap<Integer, Simulation> getSimulations() {
        return simulations;
    }

    public ObservableList<Simulation> getSimulationsList() {
        return simulationsList;
    }

    public DTOSimulationDetails getSimulationDetails(){
        return engine.getSimulationDetails();
    }

    public Collection<DTOEnvironmentVariable> getEnvironmentDefinitions(){
        return engine.getEnvironmentDefinitions();
    }
}
