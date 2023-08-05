package world.rule;

import engine.prd.PRDActivation;

public class Activation {

    protected int ticks;
    protected double probability;

    public Activation(PRDActivation prdObject) {
        ticks = prdObject.getTicks();
        probability = prdObject.getProbability();
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
