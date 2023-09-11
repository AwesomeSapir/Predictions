package engine.simulation.world.termination;

import com.sun.istack.internal.Nullable;

import java.io.Serializable;

public class Termination implements Serializable {

    private final @Nullable BySecond bySecond;
    private final @Nullable ByTicks byTicks;

    private boolean isMetByTicks = false;
    private boolean isMetBySeconds = false;

    public Termination(@Nullable ByTicks byTicks, @Nullable BySecond bySecond) {
        this.bySecond = bySecond;
        this.byTicks = byTicks;
    }

    public boolean isMet(int ticks, long seconds){
        if(bySecond != null){
            isMetBySeconds = bySecond.isMet(seconds);
        }
        if(byTicks != null){
            isMetByTicks = byTicks.isMet(ticks);
        }
        return isMetBySeconds || isMetByTicks;
    }

    public boolean isMetByTicks() {
        return isMetByTicks;
    }

    public boolean isMetBySeconds() {
        return isMetBySeconds;
    }

    public @Nullable BySecond getBySecond() {
        return bySecond;
    }

    public @Nullable ByTicks getByTicks() {
        return byTicks;
    }
}
