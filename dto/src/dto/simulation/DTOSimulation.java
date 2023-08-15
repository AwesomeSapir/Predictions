package dto.simulation;

import java.time.LocalDateTime;

public class DTOSimulation {

    private final LocalDateTime beginTime;
    private final int id;

    public DTOSimulation(LocalDateTime beginTime, int id) {
        this.beginTime = beginTime;
        this.id = id;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public int getId() {
        return id;
    }
}
