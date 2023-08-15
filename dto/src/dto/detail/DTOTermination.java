package dto.detail;

public class DTOTermination {

    private final long seconds;
    private final int ticks;

    public DTOTermination(long seconds, int ticks) {
        this.seconds = seconds;
        this.ticks = ticks;
    }

    public long getSeconds() {
        return seconds;
    }

    public int getTicks() {
        return ticks;
    }
}
