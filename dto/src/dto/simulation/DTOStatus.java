package dto.simulation;

public class DTOStatus {

    private int ticks;
    private long millis;
    private String status;
    private Exception error;

    public DTOStatus(int ticks, long millis, String status, Exception error) {
        this.ticks = ticks;
        this.millis = millis;
        this.status = status;
        this.error = error;
    }

    public int getTicks() {
        return ticks;
    }

    public long getMillis() {
        return millis;
    }

    public String getStatus() {
        return status;
    }

    public Exception getError() {
        return error;
    }
}
