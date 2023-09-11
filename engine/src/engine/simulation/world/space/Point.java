package engine.simulation.world.space;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point xy(int x, int y){
        return new Point(x, y);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
