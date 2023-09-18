package dto.simulation;

public class DTOQueueDetails {

    private final int capacity;
    private final int active;
    private final int paused;
    private final int stopped;
    private final int running;

    public DTOQueueDetails(int capacity, int active, int paused, int stopped, int running) {
        this.capacity = capacity;
        this.active = active;
        this.paused = paused;
        this.stopped = stopped;
        this.running = running;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getActive() {
        return active;
    }

    public int getPaused() {
        return paused;
    }

    public int getStopped() {
        return stopped;
    }

    public int getRunning() {
        return running;
    }
}
