package engine.world.space;

import engine.world.instance.entity.EntityInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceManager {

    private final Random random = new Random();
    private final Space space;

    public SpaceManager(int rows, int cols) {
        this.space = new Space(rows, cols);
    }

    public void putEntity(EntityInstance entity){
        space.placeEntity(entity, space.getRandomAvailableTile());
    }

    public void moveEntity(EntityInstance entity){
        Point point = Point.xy(entity.getX(), entity.getY());
        List<Point> availablePoints = new ArrayList<>(space.getAvailableAdjacentTiles(point));
        if(!availablePoints.isEmpty()) {
            Point selected = availablePoints.get(random.nextInt(availablePoints.size()));
            space.moveEntity(entity, selected);
        }
    }

    public void removeEntity(EntityInstance entity){
        space.removeEntity(entity);
    }

    public int getAvailableSize(){
        return space.getAvailableSize();
    }
    public int getTotalSize(){
        return space.getTotalSize();
    }

}
