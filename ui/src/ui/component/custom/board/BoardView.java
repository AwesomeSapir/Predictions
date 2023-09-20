package ui.component.custom.board;

import dto.detail.DTOEntity;
import dto.detail.DTOGrid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.engine.Status;

import java.io.IOException;
import java.util.*;

public class BoardView extends VBox {

    @FXML
    private GridPane gridBoard;
    @FXML
    private HBox hboxLegend;

    private EntityTile[][] board;
    private final Map<String, String> entityColors = new HashMap<>();
    private final List<String> availableColors = new ArrayList<>(Arrays.asList("blue", "red", "yellow", "green"));

    private final IntegerProperty rows = new SimpleIntegerProperty();
    private final IntegerProperty cols = new SimpleIntegerProperty();

    private EngineManager engineManager;
    private Simulation simulation;

    //TODO change to timer
    //TODO change to canvas
    private final Timeline updater = new Timeline(new KeyFrame(Duration.millis(50), event -> {
        setTiles(engineManager.getSimulationSpace(simulation).getSpace());
    }));

    public BoardView(EngineManager engineManager, Simulation simulation) {
        updater.setCycleCount(Timeline.INDEFINITE);
        this.engineManager = engineManager;
        this.simulation = simulation;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/component/custom/board/viewBoard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setSize(int rows, int cols) {
        this.rows.set(rows);
        this.cols.set(cols);
        createBoard();
    }

    private void createBoard() {
        board = new EntityTile[rows.get()][cols.get()];
        for (int row = 0; row < rows.get(); row++) {
            gridBoard.addRow(row);
        }
        for (int col = 0; col < cols.get(); col++) {
            gridBoard.addColumn(col);
        }
        for (int row = 0; row < rows.get(); row++) {
            for (int col = 0; col < cols.get(); col++) {
                EntityTile tile = new EntityTile();
                gridBoard.add(tile, col, row);
                board[row][col] = tile;
            }
        }
    }

    public void setTiles(String[][] space) {
        for (int row = 0; row < rows.get(); row++) {
            for (int col = 0; col < cols.get(); col++) {
                if (space[row][col] == null) {
                    board[row][col].setEmpty();
                } else {
                    board[row][col].setEntity(entityColors.get(space[row][col]));
                }
            }
        }
    }

    @FXML
    public void initialize() {
        DTOGrid grid = engineManager.getGrid(simulation.getId());
        setSize(grid.getRows(), grid.getCols());

        simulation.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Status.STOPPED) {
                updater.stop();
            }
        });
        for (DTOEntity entity : engineManager.getSimulationDetails().getEntities()) {
            entityColors.put(entity.getName(), availableColors.get(0));
            Label legend = new Label(entity.getName());
            legend.getStyleClass().add("label-box");
            legend.getStyleClass().add("color-" + entityColors.get(entity.getName()));
            hboxLegend.getChildren().add(legend);
            availableColors.remove(0);
        }
        updater.play();
    }

    public void stop() {
        updater.stop();
    }
}
