import world.World;
import world.definition.entity.EntityDefinition;
import world.termination.Termination;

import java.time.LocalDateTime;

public interface SimulationInterface {

    void run(int id);

    int getId();

    LocalDateTime getDate();

    Termination getTermination();

    void setEnvironmentValue(String name, Object value);

    EntityDefinition getPrimaryEntityDefinition();

    World getWorld();
}
