import world.World;
import world.instance.entity.EntityInstance;
import world.rule.Rule;
import world.rule.action.Action;

import java.util.Date;

public class Simulation implements SimulationInterface {
    private World world;
    private int id;
    private Date date;

    public Simulation(World world) {
        this.world = world;
    }

    @Override
    public void run(int id, Date date) {
        this.id = id;
        this.date = date;
        long begin = date.getTime()/1000;
        int tick = 0;

        while (!world.getTermination().isMet(tick, System.currentTimeMillis()/1000 - begin)){
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
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Date getDate() {
        return date;
    }
}
