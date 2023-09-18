package ui.engine;

import dto.detail.DTOEnvironmentVariable;
import dto.simulation.*;
import engine.Engine;
import engine.EngineInterface;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Pair;
import ui.Notify;

import java.io.File;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class EngineManager {

    public final EngineInterface engine; //TODO make private
    private final ObservableMap<Integer, Simulation> simulations = FXCollections.observableHashMap();
    private final ObservableList<Simulation> simulationsList = FXCollections.observableArrayList();
    private BooleanProperty isSimulationLoaded = new SimpleBooleanProperty(false);
    private StringProperty simulationPath = new SimpleStringProperty();
    private final Timer simulationUpdater = new Timer();
    private final Queue queue = new Queue();

    public EngineManager() {
        engine = new Engine();
        simulationUpdater.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Simulation simulation : simulationsList){
                    Platform.runLater(() -> {
                        updateQueue();
                    });
                    if(simulation.getStatus() != Status.STOPPED) {
                        Platform.runLater(() -> {
                            updateSimulationProgress(simulation);
                            //populateEntityTable(selectedSimulation.get().getId());
                        });
                    }
                }
            }
        }, 0, 200);
    }

    public Queue getQueue() {
        return queue;
    }

    public void updateQueue(){
        DTOQueueDetails queueDetails = engine.getQueueDetails();
        queue.setCapacity(queueDetails.getCapacity());
        queue.setActive(queueDetails.getActive());
        queue.setRunning(queueDetails.getRunning());
        queue.setPaused(queueDetails.getPaused());
        queue.setStopped(queueDetails.getStopped());
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
        DTOSimulation dtoSimulation = engine.runSimulation();
        Simulation simulation =  new Simulation(
                dtoSimulation.getId(),
                dtoSimulation.getBeginTime(),
                engine.getSimulationTermination(dtoSimulation.getId()));
        simulations.put(simulation.getId(), simulation);
        simulationsList.add(simulation);
    }

    public void resumeSimulation(int id){
        engine.resumeSimulation(id);
        simulations.get(id).setStatus(Status.RUNNING);
    }

    public void pauseSimulation(int id){
        engine.pauseSimulation(id);
    }

    public void stopSimulation(int id){
        engine.stopSimulation(id);
        //simulations.get(id).setStatus(Status.STOPPED);
    }

    public void updateSimulationProgress(Simulation simulation) {
        DTOStatus status = engine.getSimulationStatus(simulation.getId());
        simulation.getProgressSeconds().setValue(status.getMillis() / 1000.0);
        simulation.getProgressTicks().setValue(status.getTicks());
        simulation.setStatus(Status.valueOf(status.getStatus()));
        if(simulation.getStatus() == Status.STOPPED){
            Notify.getInstance().showAlertBar("Simulation #" + simulation.getId() + " finished.");
        }
    }

    public DTOSpace getSimulationSpace(Simulation simulation){
        return engine.getSimulationSpace(simulation.getId());
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

    public void setEntityPopulations(Collection<Pair<String, Integer>> values) {
        engine.setEntityPopulations(values);
    }

    public void tickSimulation(int id) {
        engine.tickSimulation(id);
        simulations.get(id).setStatus(Status.PAUSED);
    }
}
