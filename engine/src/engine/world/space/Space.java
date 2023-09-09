package engine.world.space;

import engine.world.instance.entity.EntityInstance;

import java.util.*;

public class Space {

    private int rows, cols;
    private Tile[][] board;
    private final Map<Point, Tile> availableTiles = new HashMap<>();
    private final Random random = new Random();

    public Space(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new Tile[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = new Tile(Point.xy(row, col));
                availableTiles.put(Point.xy(row, col), board[row][col]);
            }
        }
    }

    private Tile getTile(Point point){
        return board[point.x()%rows][point.y()%cols];
    }

    private void removeAvailableTile(Point point){
        availableTiles.remove(Point.xy(point.x()%rows, point.y()%cols));
    }

    private void addAvailableTile(Point point){
        availableTiles.put(Point.xy(point.x()%rows, point.y()%cols), getTile(point));
    }

    public void placeEntity(EntityInstance entity, Point point){
        getTile(point).setEntity(entity);
        removeAvailableTile(point);
    }

    public void moveEntity(EntityInstance entity, Point newPoint){
        removeEntity(entity);
        placeEntity(entity, newPoint);
    }

    public void removeEntity(EntityInstance entity){
        Point point = Point.xy(entity.getX(), entity.getY());
        getTile(point).removeEntity();
        addAvailableTile(point);
    }

    public Collection<Point> getAvailableAdjacentTiles(Point point){
        int x = point.x();
        int y = point.y();
        List<Point> tiles = new ArrayList<>();
        Tile[] adjacentTiles = {
                getTile(Point.xy(x-1, y)),
                getTile(Point.xy(x+1, y)),
                getTile(Point.xy(x, y-1)),
                getTile(Point.xy(x, y+1))
        };

        for (Tile t : adjacentTiles) {
            if (!t.isTaken()) {
                tiles.add(t.getPoint());
            }
        }

        return tiles;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Point getRandomAvailableTile(){
        List<Tile> tiles = new ArrayList<>(availableTiles.values());
        int index = random.nextInt(availableTiles.size());
        return tiles.get(index).getPoint();
    }

    public int getAvailableSize(){
        return availableTiles.size();
    }

    public int getTotalSize(){
        return rows*cols;
    }
}
