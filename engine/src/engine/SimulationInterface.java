package engine;

import engine.simulation.Status;
import engine.world.World;
import engine.world.definition.entity.EntityDefinition;
import engine.world.termination.Termination;

import java.time.LocalDateTime;

public interface SimulationInterface {

    void run(int id);

    int getId();

    LocalDateTime getDate();

    Termination getTermination();

    void setEnvironmentValue(String name, Object value);

    Object getEnvironmentValue(String name);

    EntityDefinition getPrimaryEntityDefinition();

    World getWorld();

    int getTick();

    long getDuration();

    Status getStatus();

    void pause();

    void resume();
}
