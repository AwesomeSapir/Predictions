package ui.engine;

import dto.detail.DTOTermination;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Simulation {

    private final int id;
    private final LocalDateTime runDate;

    private final Progress progressSeconds;
    private final Progress progressTicks;
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.RUNNING);
    private final ObservableList<EntityInfo> entities = FXCollections.observableArrayList();

    public Simulation(int id, LocalDateTime runDate, DTOTermination termination) {
        this.id = id;
        this.runDate = runDate;
        progressSeconds = new Progress(termination.getCondition("SECONDS")!=null ? ((Long) termination.getCondition("SECONDS").getCondition()).intValue() : null);
        progressTicks = new Progress(termination.getCondition("TICKS")!=null ? ((Long) termination.getCondition("TICKS").getCondition()).intValue() : null);
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getRunDate() {
        return runDate;
    }

    public Progress getProgressSeconds() {
        return progressSeconds;
    }

    public Progress getProgressTicks() {
        return progressTicks;
    }

    public Status getStatus() {
        return status.get();
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public ObservableList<EntityInfo> getEntities() {
        return entities;
    }
}
