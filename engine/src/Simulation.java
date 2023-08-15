import world.World;
import world.definition.entity.EntityDefinition;
import world.instance.entity.EntityInstance;
import world.rule.Rule;
import world.rule.action.Action;
import world.termination.Termination;

import java.time.Duration;
import java.time.LocalDateTime;

public class Simulation implements SimulationInterface {
    private final World world;
    private int id;
    private LocalDateTime date;

    public Simulation(World world) {
        this.world = world;
    }

    @Override
    public void run(int id) {
        this.id = id;

        LocalDateTime begin = LocalDateTime.now();
        int tick = 0;

        while (!world.getTermination().isMet(tick, Duration.between(begin, LocalDateTime.now()).getSeconds())){
            tick++;
            for (EntityInstance entityInstance : world.getPrimaryEntityInstances()){
                for (Rule rule : world.getRules().values()){
                    if(rule.getActivation().canBeActivated(tick)){
                        for (Action action : rule.getActions()){
                            action.execute(entityInstance, world);
                        }
                    }
                }
            }
        }
        this.date = begin;
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
}
