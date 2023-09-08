package engine.simulation;

import engine.world.World;
import engine.world.definition.entity.EntityDefinition;
import engine.world.termination.Termination;

import java.time.LocalDateTime;
import java.util.Collection;

public interface SimulationInterface {

    void run(int id);

    int getId();

    LocalDateTime getDate();

    Termination getTermination();

    void setEnvironmentValue(String name, Object value);

    Object getEnvironmentValue(String name);

    EntityDefinition getEntityDefinition(String name);

    World getWorld();

    int getTick();

    long getDuration();

    void pause();

    void resume();

    public Collection<EntityDefinition> getAllEntityDefinitions();
}
