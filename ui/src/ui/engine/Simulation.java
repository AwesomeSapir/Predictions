package ui.engine;

import dto.detail.DTOTermination;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

public class Simulation {

    private final int id;
    private final LocalDateTime runDate;

    private final Progress progressSeconds;
    private final Progress progressTicks;
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.RUNNING);

    public Simulation(int id, LocalDateTime runDate, DTOTermination termination) {
        this.id = id;
        this.runDate = runDate;
        progressSeconds = new Progress(termination.getCondition("SECONDS")!=null ? (Integer) termination.getCondition("SECONDS").getCondition() : null);
        progressTicks = new Progress(termination.getCondition("TICKS")!=null ? (Integer) termination.getCondition("TICKS").getCondition() : null);
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
}
