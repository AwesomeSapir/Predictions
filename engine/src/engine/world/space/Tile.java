package engine.world.space;

import engine.world.instance.entity.EntityInstance;

public class Tile {

    private Point point;
    private boolean taken;
    private EntityInstance entity;

    public Tile(Point point) {
        this.point = point;
    }

    public void removeEntity(){
        entity = null;
        taken = false;
    }

    public boolean isTaken() {
        return taken;
    }

    public EntityInstance getEntity() {
        return entity;
    }

    public void setEntity(EntityInstance entity) {
        this.entity = entity;
        entity.setXY(point.x(), point.y());
        taken = true;
    }

    public Point getPoint() {
        return point;
    }

    public int getX() {
        return point.x();
    }

    public int getY() {
        return point.y();
    }
}
