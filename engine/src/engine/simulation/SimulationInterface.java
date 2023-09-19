package engine.simulation;

import engine.simulation.world.World;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.termination.Termination;
import exception.runtime.IllegalActionException;
import exception.runtime.IllegalUserActionException;
import exception.runtime.SimulationRuntimeException;

import java.time.LocalDateTime;
import java.util.Collection;

public interface SimulationInterface extends Runnable {

    void run(int id);

    void next() throws IllegalUserActionException, IllegalActionException;

    int getId();

    LocalDateTime getDate();

    Termination getTermination();

    void setEnvironmentValue(String name, Object value);

    void initSpace() throws IllegalActionException;

    Object getEnvironmentValue(String name);

    EntityDefinition getEntityDefinition(String name) throws IllegalActionException;

    World getWorld();

    int getTick();

    long getDuration();

    void stop() throws IllegalUserActionException;

    void singleTick();

    Status getStatus();

    void pause() throws IllegalUserActionException;

    void resume() throws IllegalUserActionException;

    Collection<EntityDefinition> getAllEntityDefinitions();

    void setEntityPopulation(String name, int population) throws IllegalActionException;

    SimulationRuntimeException getException();
}
