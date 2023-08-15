package dto.simulation;

public class DTOSimulationResult {

    private final boolean isBySeconds;
    private final boolean isByTicks;
    private final int id;

    public DTOSimulationResult(boolean isBySeconds, boolean isByTicks, int id) {
        this.isBySeconds = isBySeconds;
        this.isByTicks = isByTicks;
        this.id = id;
    }

    public boolean isBySeconds() {
        return isBySeconds;
    }

    public boolean isByTicks() {
        return isByTicks;
    }

    public int getId() {
        return id;
    }
}
