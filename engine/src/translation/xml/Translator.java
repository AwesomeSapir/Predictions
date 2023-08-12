package translation.xml;

import world.World;

import java.io.InvalidClassException;

public interface Translator {
    World getWorld() throws InvalidClassException;
}
