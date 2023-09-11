package engine.simulation;

import engine.world.World;
import engine.world.definition.entity.EntityDefinition;
import engine.world.termination.Termination;

import java.time.LocalDateTime;
import java.util.Collection;

public interface SimulationInterface extends Runnable {

    void run(int id);

    void next();

    int getId();

    LocalDateTime getDate();

    Termination getTermination();

    void setEnvironmentValue(String name, Object value);

    void initSpace();

    Object getEnvironmentValue(String name);

    EntityDefinition getEntityDefinition(String name);

    World getWorld();

    int getTick();

    long getDuration();

    void stop();

    Status getStatus();

    void pause();

    void resume();

    Collection<EntityDefinition> getAllEntityDefinitions();

    void setEntityPopulation(String name, int population);
}
