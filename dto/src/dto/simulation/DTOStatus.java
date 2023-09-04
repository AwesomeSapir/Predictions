package dto.simulation;

public class DTOStatus {

    private int ticks;
    private long seconds;

    public DTOStatus(int ticks, long seconds) {
        this.ticks = ticks;
        this.seconds = seconds;
    }

    public int getTicks() {
        return ticks;
    }

    public long getSeconds() {
        return seconds;
    }
}
