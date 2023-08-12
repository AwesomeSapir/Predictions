package world.termination;

import com.sun.istack.internal.Nullable;

public class Termination {

    private final BySecond bySecond;
    private final ByTicks byTicks;

    public Termination(@Nullable ByTicks byTicks, @Nullable BySecond bySecond) {
        this.bySecond = bySecond;
        this.byTicks = byTicks;
    }

    public boolean isMet(int ticks, long seconds){
        boolean isMetByTicks = false;
        boolean isMetBySeconds = false;
        if(bySecond != null){
            isMetBySeconds = bySecond.isMet(seconds);
        }
        if(byTicks != null){
            isMetByTicks = byTicks.isMet(ticks);
        }
        return isMetBySeconds || isMetByTicks;
    }
}
