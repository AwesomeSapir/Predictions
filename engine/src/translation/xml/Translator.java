package translation.xml;

import engine.simulation.world.World;
import exception.FatalException;
import exception.XMLConfigException;
import exception.runtime.IllegalActionException;
import exception.runtime.IncompatibleTypesException;

public interface Translator {
    World getWorld() throws XMLConfigException, FatalException, IncompatibleTypesException, IllegalActionException;
}
