package engine;

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
    private long duration;
    private LocalDateTime date;

    public Simulation(World world) {
        this.world = world;
    }

    @Override
    public void run(int id) {
        this.id = id;
        LocalDateTime begin = LocalDateTime.now();
        this.date = begin;

        while (!world.getTermination().isMet(tick, duration)){
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
            duration = Duration.between(begin, LocalDateTime.now()).getSeconds();
        }

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
        return duration;
    }
}
