package engine.simulation;

import engine.simulation.world.World;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.rule.Rule;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.termination.BySecond;
import engine.simulation.world.termination.ByTicks;
import engine.simulation.world.termination.ByUser;
import engine.simulation.world.termination.Termination;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Simulation implements SimulationInterface, Serializable {
    private final World world;
    private int id;
    private int tick = 0;
    private long totalDuration = 0;
    private LocalDateTime date;

    private Status status;

    public Simulation(World world) {
        this.world = world;
    }

    @Override
    public void run(int id) {
        this.id = id;
        this.date = LocalDateTime.now();
        this.status = Status.RUNNING;
    }

    private void loop() {
        while (status == Status.RUNNING) {
            tick();
        }
    }

    public void updateTermination() {
        if (getTermination().getTerminationCondition(Termination.Type.TICKS) != null) {
            ((ByTicks) getTermination().getTerminationCondition(Termination.Type.TICKS)).setValue(tick);
        }
        if (getTermination().getTerminationCondition(Termination.Type.SECONDS) != null) {
            ((BySecond) getTermination().getTerminationCondition(Termination.Type.SECONDS)).setValue(totalDuration / 1000);
        }
    }

    public void tick() {
        if (world.getTermination().isMet()) {
            status = Status.STOPPED;
            return;
        }
        tick++;

        LocalDateTime begin = LocalDateTime.now();

        List<EntityInstance> entityInstances = new ArrayList<>(world.getEntityManager().getAllEntityInstances());
        List<Action> validActions = new ArrayList<>();

        for (EntityInstance entityInstance : entityInstances) {
            for (PropertyInstance propertyInstance : entityInstance.getPropertyInstances()) {
                propertyInstance.updateTicksOfSameValue();
                //System.out.println("Property '" + propertyInstance.getPropertyDefinition().getName() +"' didn't change for '" +propertyInstance.getTicksOfSameValue() + "' ticks");
            }
            world.getSpaceManager().moveEntity(entityInstance);
        }

        for (Rule rule : world.getRules().values()) {
            if (rule.getActivation().canBeActivated(tick)) {
                validActions.addAll(rule.getActions());
            }
        }

        for (EntityDefinition entityDefinition : world.getEntityManager().getAllEntityDefinitions()) {
            for (Action action : validActions) {
                if (action.getPrimaryEntity().equals(entityDefinition)) {
                    for (EntityInstance entityInstance : world.getEntityManager().getEntityInstances(entityDefinition)) {
                        if (action.getSecondaryEntity() != null) {
                            for (EntityInstance secondaryEntity : world.getEntityManager().getEntityInstances(action.getSecondaryEntity(), world)) {
                                action.execute(entityInstance, secondaryEntity, world);
                            }
                        } else {
                            action.execute(entityInstance, world);
                        }
                    }
                }

            }
        }

        world.getEntityManager().killEntities();
        world.getEntityManager().createEntities();

        totalDuration += Duration.between(begin, LocalDateTime.now()).toMillis();
        updateTermination();
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
    public void setEntityPopulation(String name, int population) {
        EntityDefinition entityDefinition = world.getEntityManager().getEntityDefinition(name);
        world.getEntityManager().setPopulation(entityDefinition, population);
    }

    @Override
    public void initSpace() {
        for (EntityInstance entityInstance : world.getEntityManager().getAllEntityInstances()) {
            world.getSpaceManager().putEntity(entityInstance);
        }
    }

    @Override
    public Object getEnvironmentValue(String name) {
        return world.getEnvironmentPropertyInstance(name).getValue();
    }

    @Override
    public EntityDefinition getEntityDefinition(String name) {
        return world.getEntityManager().getEntityDefinition(name);
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
    public long getDuration() {
        return totalDuration;
    }

    @Override
    public void pause() {
        if (status == Status.RUNNING) {
            status = Status.PAUSED;
        } else {
            throw new RuntimeException("Simulation isn't running.");
        }
    }

    @Override
    public void resume() {
        if (status == Status.PAUSED) {
            status = Status.RUNNING;
        } else {
            throw new RuntimeException("Simulation isn't paused.");
        }
    }

    @Override
    public void stop() {
        if (status == Status.RUNNING || status == Status.PAUSED) {
            status = Status.STOPPED;
            if (getTermination().getTerminationCondition(Termination.Type.USER) != null) {
                ((ByUser) getTermination().getTerminationCondition(Termination.Type.USER)).stop();
            }
        } else {
            throw new RuntimeException("Simulation isn't running.");
        }
    }

    @Override
    public void next() {
        if (status != Status.STOPPED) {
            status = Status.RUNNING;
            tick();
            if (status != Status.STOPPED) {
                status = Status.PAUSED;
            }
        } else {
            throw new RuntimeException("Simulation is stopped.");
        }
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Collection<EntityDefinition> getAllEntityDefinitions() {
        return world.getEntityManager().getAllEntityDefinitions();
    }

    @Override
    public void run() {
        loop();
    }
}
