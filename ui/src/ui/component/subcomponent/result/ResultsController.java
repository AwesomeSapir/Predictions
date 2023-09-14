package ui.component.subcomponent.result;

import dto.simulation.DTOEntityPopulation;
import dto.simulation.DTOSimulationResult;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import ui.component.custom.board.BoardView;
import ui.component.custom.progress.SimulationProgressView;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.engine.Status;
import ui.style.StyleManager;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class ResultsController {
    @FXML public TextArea textResult;
    @FXML public ListView<Simulation> listExecution;
    @FXML public SimulationProgressView gridSeconds;
    @FXML public SimulationProgressView gridTicks;
    @FXML public Button buttonResume;
    @FXML public Button buttonPause;
    @FXML public Button buttonStop;
    @FXML public Button buttonRerun;
    @FXML public ScrollPane paneDetails;

    private final ObjectProperty<Simulation> selectedSimulation = new SimpleObjectProperty<>();
    private final ObjectProperty<Status> selectedStatus = new SimpleObjectProperty<>();
    @FXML public Button buttonBoard;
    @FXML public Button buttonNext;
    @FXML public LineChart entityAmountByTicks;

    @FXML private TableView<EntityInfo> entityTable;

    @FXML private TableColumn<EntityInfo, String> entityNameColumn;

    @FXML private TableColumn<EntityInfo, Integer> instanceCountColumn;

    //Preparing the data points for the line1
    private Map<Integer, XYChart.Series> seriesMap = new HashMap<>();



    private EngineManager engineManager;

    //TODO replace with thread
    private final Timeline updater = new Timeline(new KeyFrame(Duration.millis(100), event -> {
        engineManager.updateSimulationProgress(selectedSimulation.get());
        populateEntityTable(selectedSimulation.get().getId());
        //selectedSimulation.get().setStatus(Status.valueOf(engineManager.engine.getSimulationStatus(selectedSimulation.get().getId()).getStatus()));
    }));

    private final ChangeListener<Status> simulationStatusListener = (observable, oldValue, newValue) -> {
        if (newValue != Status.RUNNING) {
            updater.stop();
            showSimulationResult(engineManager.engine.getSimulationResult(selectedSimulation.get().getId()));
            engineManager.updateSimulationProgress(selectedSimulation.get());
        } else {
            updater.play();
        }
    };

    public ResultsController() {
        updater.setCycleCount(Timeline.INDEFINITE);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        listExecution.setItems(engineManager.getSimulationsList());
        entityAmountByTicks.setTitle("Quantity as a function of ticks");
        selectedSimulation.addListener((observable, oldValue, newValue) -> {
            gridSeconds.setProgress(newValue.getProgressSeconds());
            gridTicks.setProgress(newValue.getProgressTicks());
            entityAmountByTicks.visibleProperty().unbind();
            entityAmountByTicks.setVisible(false);
            selectedStatus.unbind();
            selectedStatus.bind(newValue.statusProperty());
            entityAmountByTicks.getData().clear();
            showSimulationResult(engineManager.engine.getSimulationResult(newValue.getId()));
            entityAmountByTicks.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));

            populateEntityTable(newValue.getId());
        });
    }

    public void showSimulationResult(DTOSimulationResult simulationResult) {
        String result;
        if (simulationResult == null) {
            result = "Simulation is still running...";
        } else {
            result = "Simulation result:\n" + "Id: " + simulationResult.getId() + "\nTermination reason:";
            if (simulationResult.isBySeconds()) {
                result += " by seconds\n";
            }
            if (simulationResult.isByTicks()) {
                result += " by ticks\n";
            }

            for (DTOEntityPopulation entityPopulation : engineManager.engine.getDetailsByEntityCount(simulationResult.getId())) {
                result += "\nEntity Name: " + entityPopulation.getEntity().getName();
                result += "\nInitial Quantity: " + entityPopulation.getInitialPopulation();
                result += "\nFinal Quantity: " + entityPopulation.getFinalPopulation();
            }
            // Get or create the series for the current simulation ID
            entityAmountByTicks.getData().add(seriesMap.get(simulationResult.getId()));
        }
        textResult.textProperty().set(result);
    }

    @FXML
    public void initialize() {
        selectedSimulation.bind(listExecution.getSelectionModel().selectedItemProperty());
        selectedStatus.addListener(simulationStatusListener);
        entityAmountByTicks.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        buttonPause.setOnAction(this::actionSimulationPause);
        buttonResume.setOnAction(this::actionSimulationResume);
        buttonStop.setOnAction(this::actionSimulationStop);
        buttonRerun.setOnAction(this::actionSimulationRerun);
        buttonBoard.setOnAction(this::actionShowBoard);
        buttonNext.setOnAction(this::actionSimulationNext);

        buttonPause.disableProperty().bind(selectedStatus.isEqualTo(Status.RUNNING).not());
        buttonResume.disableProperty().bind(selectedStatus.isEqualTo(Status.PAUSED).not());
        buttonStop.disableProperty().bind(Bindings.and(
                selectedStatus.isNotEqualTo(Status.RUNNING),
                selectedStatus.isNotEqualTo(Status.PAUSED)));
        buttonRerun.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED).not());
        buttonNext.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));

        listExecution.setCellFactory(new Callback<ListView<Simulation>, ListCell<Simulation>>() {
            @Override
            public ListCell<Simulation> call(ListView<Simulation> param) {
                return new ListCell<Simulation>() {
                    @Override
                    protected void updateItem(Simulation item, boolean empty) {
                        String runDate = "";
                        if(item != null) {
                            runDate = item.getRunDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss"));
                        }
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("Simulation #" + item.getId() + "   " + runDate);
                        }
                    }
                };
            }
        });

        entityTable.setFixedCellSize(25);
        entityTable.prefHeightProperty().bind(Bindings.size(entityTable.getItems()).multiply(entityTable.getFixedCellSize()).add(30));
        // Bind the columns to the EntityInfo properties
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        instanceCountColumn.setCellValueFactory(new PropertyValueFactory<>("instanceCount"));

        updater.statusProperty().addListener((observable, oldValue, newValue) -> System.out.println("Updater is now " + newValue + " was " + oldValue));
    }

    private void actionSimulationPause(ActionEvent actionEvent) {
        engineManager.pauseSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationStop(ActionEvent actionEvent) {
        engineManager.stopSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationResume(ActionEvent actionEvent) {
        engineManager.resumeSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationRerun(ActionEvent actionEvent){

    }

    private void actionShowBoard(ActionEvent actionEvent){
        Stage stage = new Stage();
        BoardView boardView = new BoardView(engineManager, selectedSimulation.get());
        boardView.setSize(100, 100);
        Scene scene = new Scene(boardView);
        StyleManager.register(scene);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            boardView.stop();
            StyleManager.unregister(scene);
        });
        stage.show();
    }

    private void actionSimulationNext(ActionEvent actionEvent){
        engineManager.tickSimulation(selectedSimulation.get().getId());
        engineManager.updateSimulationProgress(selectedSimulation.get());
    }

    private void populateEntityTable(int simulationId) {
        // Clear existing items in the table and reset the series
        entityTable.getItems().clear();

        // Get or create the series for the current simulation ID
        XYChart.Series series = seriesMap.computeIfAbsent(simulationId, k -> new XYChart.Series());

        // Get entity information from the engineManager
        Collection<DTOEntityPopulation> entityPopulations = engineManager.engine.getDetailsByEntityCount(simulationId);

        // Populate the table with entity information
        for (DTOEntityPopulation entityPopulation : entityPopulations) {
            int population = entityPopulation.getFinalPopulation();
            double ticks = engineManager.getSimulations().get(simulationId).getProgressTicks().getValue();
            entityTable.getItems().add(new EntityInfo(entityPopulation.getEntity().getName(), population));

            if(ticks % 10 == 0) {
                // Add data points to the LineChart series
                series.getData().add(new XYChart.Data<>(ticks, population));
            }
        }
    }
}
