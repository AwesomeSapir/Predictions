package ui.engine;

import dto.detail.DTOTermination;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Simulation {

    private final int id;
    private final Progress progressSeconds;
    private final Progress progressTicks;
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.RUNNING);

    public Simulation(int id, DTOTermination termination) {
        this.id = id;
        progressSeconds = new Progress(termination.getSeconds()!=null ? termination.getSeconds().intValue() : null);
        progressTicks = new Progress(termination.getTicks()!=null ? termination.getTicks().intValue() : null);
    }

    public int getId() {
        return id;
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
