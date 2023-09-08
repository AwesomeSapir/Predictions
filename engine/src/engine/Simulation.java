package engine;

import engine.simulation.Status;
import engine.world.World;
import engine.world.definition.entity.EntityDefinition;
import engine.world.instance.entity.EntityInstance;
import engine.world.rule.Rule;
import engine.world.rule.action.Action;
import engine.world.termination.Termination;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements SimulationInterface, Serializable {
    private final World world;
    private int id;
    private int tick = 0;
    private long currentDuration = 0;
    private long totalDuration = 0;
    private LocalDateTime date;
    private Status status;

    private boolean paused = false;

    public Simulation(World world) {
        this.world = world;
    }

    @Override
    public void run(int id) {
        this.id = id;
        this.date = LocalDateTime.now();
        status = Status.RUNNING;
        loop();
    }

    private void loop(){
        LocalDateTime begin = LocalDateTime.now();

        while (status == Status.RUNNING){
            if(world.getTermination().isMet(tick, (currentDuration + totalDuration)/1000)){
                status = Status.STOPPED;
                break;
            }
            tick++;
            List<EntityInstance> entityInstances = new ArrayList<>(world.getPrimaryEntityInstances());
            for (int i = 0; i < world.getPrimaryEntityInstances().size(); i++) {
                for (Rule rule : world.getRules().values()){
                    if(rule.getActivation().canBeActivated(tick)){
                        for (Action action : rule.getActions()){
                            action.execute(entityInstances.get(i), world);
                        }
                    }
                }
            }
            currentDuration = Duration.between(begin, LocalDateTime.now()).toMillis();
        }
        totalDuration += currentDuration;
        currentDuration = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public Termination getTermination() {
        return world.getTermination();
    }

    @Override
    public void setEnvironmentValue(String name, Object value) {
        world.getEnvironmentPropertyInstance(name).setValue(value);
    }

    @Override
    public Object getEnvironmentValue(String name) {
        return world.getEnvironmentPropertyInstance(name).getValue();
    }

    @Override
    public EntityDefinition getPrimaryEntityDefinition() {
        return world.getPrimaryEntityDefinition();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getTick() {
        return tick;
    }

    @Override
    public long getDuration(){
        return (totalDuration + currentDuration)/1000;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void pause() {
        if(!paused) {
            paused = true;
            status = Status.PAUSED;
        } else {
            throw new RuntimeException("Simulation isn't running.");
        }
    }

    @Override
    public void resume() {
        if(paused) {
            paused = false;
            status = Status.RUNNING;
            loop();
        } else {
            throw new RuntimeException("Simulation isn't paused.");
        }
    }
}
