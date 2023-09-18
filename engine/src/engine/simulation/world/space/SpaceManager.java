package engine.simulation.world.space;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;

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
        space.placeEntityRandom(entity);
    }

    public void moveEntity(EntityInstance entity){
        Point point = Point.xy(entity.getX(), entity.getY());
        List<Point> availablePoints = new ArrayList<>(space.getAvailableAdjacentTiles(point));
        if(!availablePoints.isEmpty()) {
            Point selected = availablePoints.get(random.nextInt(availablePoints.size()));
            space.moveEntity(entity, selected);
        }
    }

    public int getRows(){
        return space.getRows();
    }

    public int getCols(){
        return space.getCols();
    }

    public void removeEntity(EntityInstance entity){
        space.removeEntity(entity);
    }

    private void placeEntity(EntityInstance entity, Point point){
        space.placeEntity(entity, point);
    }

    public EntityInstance getEntityInProximity(Point source, EntityDefinition target, double depth){
        for (EntityInstance entityInstance : space.getEntitiesInProximity(source, depth)){
            if(entityInstance.getEntityDefinition().equals(target)){
                return entityInstance;
            }
        }
        return null;
    }

    public int getAvailableSize(){
        return space.getAvailableSize();
    }
    public int getTotalSize(){
        return space.getTotalSize();
    }

    public void replaceEntity(EntityInstance oldEntity, EntityInstance newEntity) {
        Point point = oldEntity.getPoint();
        removeEntity(oldEntity);
        placeEntity(newEntity, point);
    }
}
