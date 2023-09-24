package translation.xml;

import engine.simulation.world.WorldDefinition;
import exception.FatalException;
import exception.XMLConfigException;
import exception.runtime.IllegalActionException;
import exception.runtime.IncompatibleTypesException;

public interface Translator {
    WorldDefinition getWorld() throws XMLConfigException, FatalException, IncompatibleTypesException, IllegalActionException;
}
