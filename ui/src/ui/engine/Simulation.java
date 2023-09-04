package ui.engine;

import dto.detail.DTOTermination;
import dto.simulation.DTOSimulationResult;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Simulation {

    private final int id;
    private DTOSimulationResult result;
    private Progress progressSeconds;
    private Progress progressTicks;
    private final BooleanProperty resultReady = new SimpleBooleanProperty(false);

    public Simulation(int id, DTOTermination termination) {
        this.id = id;
        progressSeconds = new Progress(termination.getSeconds());
        progressTicks = new Progress(termination.getTicks());
    }

    public int getId() {
        return id;
    }

    public DTOSimulationResult getResult() {
        return result;
    }

    public void setResult(DTOSimulationResult result) {
        this.result = result;
        resultReady.set(true);
    }

    public boolean isResultReady() {
        return resultReady.get();
    }

    public BooleanProperty resultReadyProperty() {
        return resultReady;
    }

    public Progress getProgressSeconds() {
        return progressSeconds;
    }

    public Progress getProgressTicks() {
        return progressTicks;
    }
}
