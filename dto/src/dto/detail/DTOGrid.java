package dto.detail;

public class DTOGrid extends DTOObject {
    private final int rows;
    private final int cols;

    public DTOGrid(int rows, int cols) {
        super("Grid");
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
