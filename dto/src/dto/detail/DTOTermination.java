package dto.detail;

public class DTOTermination {

    private final Long seconds;
    private final Long ticks;

    public DTOTermination(Long seconds, Long ticks) {
        this.seconds = seconds;
        this.ticks = ticks;
    }

    public Long getSeconds() {
        return seconds;
    }

    public Long getTicks() {
        return ticks;
    }
}
