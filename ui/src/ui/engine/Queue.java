package ui.engine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Queue {

    private final IntegerProperty capacity = new SimpleIntegerProperty(0);
    private final IntegerProperty active = new SimpleIntegerProperty(0);
    private final IntegerProperty paused = new SimpleIntegerProperty(0);
    private final IntegerProperty stopped = new SimpleIntegerProperty(0);
    private final IntegerProperty running = new SimpleIntegerProperty(0);

    public int getCapacity() {
        return capacity.get();
    }

    public IntegerProperty capacityProperty() {
        return capacity;
    }

    public int getActive() {
        return active.get();
    }

    public IntegerProperty activeProperty() {
        return active;
    }

    public int getPaused() {
        return paused.get();
    }

    public IntegerProperty pausedProperty() {
        return paused;
    }

    public int getStopped() {
        return stopped.get();
    }

    public IntegerProperty stoppedProperty() {
        return stopped;
    }

    public int getRunning() {
        return running.get();
    }

    public IntegerProperty runningProperty() {
        return running;
    }

    public void setCapacity(int capacity) {
        this.capacity.set(capacity);
    }

    public void setActive(int active) {
        this.active.set(active);
    }

    public void setPaused(int paused) {
        this.paused.set(paused);
    }

    public void setStopped(int stopped) {
        this.stopped.set(stopped);
    }

    public void setRunning(int running) {
        this.running.set(running);
    }
}
