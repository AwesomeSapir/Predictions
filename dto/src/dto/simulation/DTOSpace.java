package dto.simulation;

public class DTOSpace {

    private final String[][] space;

    public DTOSpace(int rows, int cols) {
        space = new String[rows][cols];
    }

    public void setTile(String name, int row, int col){
        space[row][col] = name;
    }

    public String[][] getSpace() {
        return space;
    }
}
