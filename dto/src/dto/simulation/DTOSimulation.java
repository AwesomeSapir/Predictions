package dto.simulation;

import java.time.LocalDateTime;

public class DTOSimulation {

    private final LocalDateTime beginTime;
    private final int id;
    private final String status;

    public DTOSimulation(LocalDateTime beginTime, int id, String status) {
        this.beginTime = beginTime;
        this.id = id;
        this.status = status;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
