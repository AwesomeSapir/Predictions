package ui.engine;

import dto.detail.DTOEnvironmentVariable;
import dto.detail.DTOGrid;
import dto.detail.DTOProperty;
import dto.simulation.*;
import engine.Engine;
import engine.EngineInterface;
import exception.EngineException;
import exception.FatalException;
import exception.SimulationMissingException;
import exception.XMLConfigException;
import exception.runtime.IllegalActionException;
import exception.runtime.IllegalUserActionException;
import exception.runtime.IncompatibleTypesException;
import exception.runtime.SimulationRuntimeException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import ui.Notify;
import ui.component.subcomponent.result.EntityInfo;

import java.io.File;
import java.util.*;

public class EngineManager {

    private final EngineInterface engine; //TODO make private
    private final ObservableMap<Integer, Simulation> simulations = FXCollections.observableHashMap();
    private final List<Simulation> simulationsRunning = new ArrayList<>();
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
                List<Simulation> finished = new ArrayList<>();
                for (Simulation simulation : simulationsRunning){
                    Platform.runLater(() -> {
                        updateQueue();
                        updateSimulationProgress(simulation);
                        updateEntityPopulations(simulation.getId());
                        //populateEntityTable(selectedSimulation.get().getId());
                    });
                    if(simulation.getStatus() == Status.STOPPED || simulation.getStatus() == Status.ERROR) {
                        finished.add(simulation);
                    }
                }
                simulationsRunning.removeAll(finished);
                finished.clear();
            }
        }, 0, 200);
    }

    private void alertException(Exception e){
        if(e instanceof SimulationMissingException){
            throw new RuntimeException(e);
        } else if(e instanceof SimulationRuntimeException){
            alertSimulationRuntimeException((SimulationRuntimeException) e);
        } else if (e instanceof EngineException) {
            alertEngineException((EngineException) e);
        } else {
            throw new RuntimeException(e);
        }
    }

    private void alertSimulationRuntimeException(SimulationRuntimeException exception){
        Notify.getInstance().showAlertDialog(exception.getType(), exception.getSecondaryType(), exception.getMessage(), Alert.AlertType.ERROR);
    }

    private void alertEngineException(EngineException exception){
        Notify.getInstance().showAlertDialog(exception.getType(), null, exception.getMessage(), Alert.AlertType.ERROR);
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
        try {
            engine.loadXml(file.getAbsolutePath());
        }catch (IllegalActionException | FatalException | XMLConfigException | IncompatibleTypesException e) {
            alertException(e);
            return;
        }
        isSimulationLoaded.set(false);
        isSimulationLoaded.set(true);
        simulationPath.set(file.getAbsolutePath());
        Notify.getInstance().showAlertBar("File loaded successfully");
    }

    public void setEnvironmentValues(Collection<Pair<String, Object>> values){
        try {
            engine.setEnvironmentValues(values);
        } catch (SimulationMissingException e) {
            alertEngineException(e);
        }
    }

    public void runSimulation(){
        DTOSimulation dtoSimulation = null;
        try {
            dtoSimulation = engine.runSimulation();
        } catch (FatalException | SimulationMissingException | IllegalActionException | IllegalUserActionException |
                 IncompatibleTypesException | XMLConfigException e) {
            alertException(e);
        }
        Simulation simulation =  new Simulation(
                dtoSimulation.getId(),
                dtoSimulation.getBeginTime(),
                engine.getSimulationTermination(dtoSimulation.getId()));
        simulations.put(simulation.getId(), simulation);
        simulationsList.add(simulation);
        simulationsRunning.add(simulation);
        createEntityPopulations(simulation.getId());
    }

    private void createEntityPopulations(int id){
        for (DTOEntityPopulation entity : engine.getEntityPopulations(id).values()){
            EntityInfo entityInfo = new EntityInfo(entity.getEntity().getName(), entity.getInitialPopulation());
            simulations.get(id).getEntities().add(entityInfo);
        }
    }

    public void updateEntityPopulations(int id){
        Map<String, DTOEntityPopulation> entityPopulationMap = engine.getEntityPopulations(id);
        for (EntityInfo entity : simulations.get(id).getEntities()){
            entity.setInstanceCount(entityPopulationMap.get(entity.getEntityName()).getFinalPopulation());
        }
    }

    public void resumeSimulation(int id){
        try {
            engine.resumeSimulation(id);
        } catch (IllegalUserActionException e) {
            alertException(e);
        }
        simulations.get(id).setStatus(Status.RUNNING);
    }

    public void pauseSimulation(int id){
        try {
            engine.pauseSimulation(id);
        } catch (IllegalUserActionException e) {
            alertException(e);
        }
    }

    public void stopSimulation(int id){
        try {
            engine.stopSimulation(id);
        } catch (IllegalUserActionException e) {
            alertException(e);
        }
        //simulations.get(id).setStatus(Status.STOPPED);
    }

    public void updateSimulationProgress(Simulation simulation) {
        DTOStatus status = engine.getSimulationStatus(simulation.getId());
        simulation.getProgressSeconds().setValue(status.getMillis() / 1000.0);
        simulation.getProgressTicks().setValue(status.getTicks());
        Status newStatus = Status.valueOf(status.getStatus());
        if(simulation.getStatus() != newStatus) {
            simulation.setStatus(newStatus);
            if (simulation.getStatus() == Status.STOPPED) {
                Notify.getInstance().showAlertBar("Simulation #" + simulation.getId() + " finished.");
            } else if(simulation.getStatus() == Status.ERROR){
                Notify.getInstance().showAlertBar("Simulation #" + simulation.getId() + " encountered an error: " + status.getError().getMessage());
            }
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

    public Map<String, DTOEntityPopulation> getDetailsByEntityCount(int id){
        return engine.getEntityPopulations(id);
    }

    public Collection<DTOProperty> getPastEntityProperties(int id, String entity){
        try {
            return engine.getPastEntityProperties(id, entity);
        } catch (IllegalActionException e) {
            alertException(e);
        }
        return null;
    }

    public DTOSimulationHistogram getValuesForPropertyHistogram(int id, String entity, String property){
        try {
            return engine.getValuesForPropertyHistogram(id, property,entity);
        } catch (IllegalActionException e) {
            alertException(e);
        }
        return null;
    }

    public Collection<Double> getConsistencyOfProperty(int id, String entity,String property){
        try {
            return engine.getConsistencyOfProperty(id, property,entity);
        } catch (IllegalActionException e) {
            alertException(e);
        }
        return null;
    }

    public DTOSimulationDetails getSimulationDetails(){
        try {
            return engine.getSimulationDetails();
        } catch (SimulationMissingException e) {
            alertException(e);
        }
        return null;
    }

    public DTOSimulationResult getSimulationResult(int id){
        return engine.getSimulationResult(id);
    }

    public Collection<DTOEnvironmentVariable> getEnvironmentDefinitions(){
        try {
            return engine.getEnvironmentDefinitions();
        } catch (SimulationMissingException e) {
            alertException(e);
        }
        return null;
    }

    public void setEntityPopulations(Collection<Pair<String, Integer>> values) {
        try {
            engine.setEntityPopulations(values);
        } catch (IllegalActionException | SimulationMissingException e) {
            alertException(e);
        }
    }

    public void tickSimulation(int id) {
        engine.tickSimulation(id);
        simulations.get(id).setStatus(Status.PAUSED);
    }

    public DTOGrid getGrid(int id){
        return engine.getGrid(id);
    }
}
