package dto.detail;

public class DTOTermination {

    private final Long seconds;
    private final Integer ticks;

    public DTOTermination(Long seconds, Integer ticks) {
        this.seconds = seconds;
        this.ticks = ticks;
    }

    public Long getSeconds() {
        return seconds;
    }

    public Integer getTicks() {
        return ticks;
    }
}
