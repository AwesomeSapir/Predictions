package translation.xml;

import engine.simulation.world.World;

import java.io.InvalidClassException;

public interface Translator {
    World getWorld() throws InvalidClassException;
}
