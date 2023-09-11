package engine.simulation.world.rule;

import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.Random;

public class Activation implements Serializable {

    private final Random random = new Random();
    protected final int ticks;
    protected final double probability;

    public Activation(@Nullable Integer ticks, @Nullable Double probability) {
        if(ticks != null){
            this.ticks = ticks;
        } else {
            this.ticks = 1;
        }

        if(probability != null) {
            this.probability = probability;
        }else {
            this.probability = 1;
        }
    }

    public boolean canBeActivated(int ticks){
        return (ticks % this.ticks == 0)  &&  sufficientProbability();
    }

    private boolean sufficientProbability(){
        double randomProbability = random.nextDouble();
        return randomProbability < this.probability;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }
}
