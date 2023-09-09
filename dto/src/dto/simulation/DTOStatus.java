package dto.simulation;

public class DTOStatus {

    private int ticks;
    private long millis;
    private String status;

    public DTOStatus(int ticks, long millis, String status) {
        this.ticks = ticks;
        this.millis = millis;
        this.status = status;
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
}
