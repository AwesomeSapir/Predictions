package ui.component.subcomponent.result;

import dto.simulation.DTOEntityPopulation;
import dto.simulation.DTOSimulationResult;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import ui.component.custom.board.BoardView;
import ui.component.custom.node.IconButton;
import ui.component.custom.node.ToggleIconButton;
import ui.component.custom.node.State;
import ui.component.custom.progress.SimulationProgressView;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.engine.Status;
import ui.style.Animations;
import ui.style.StyleManager;

public class ResultsController {
    @FXML public TextArea textResult;
    @FXML public ListView<Simulation> listExecution;
    @FXML public SimulationProgressView gridSeconds;
    @FXML public SimulationProgressView gridTicks;
    @FXML public ToggleIconButton buttonPlayPause;
    @FXML public IconButton buttonStop;
    @FXML public IconButton buttonRerun;
    @FXML public ScrollPane paneDetails;

    private final ObjectProperty<Simulation> selectedSimulation = new SimpleObjectProperty<>();
    private final ObjectProperty<Status> selectedStatus = new SimpleObjectProperty<>();
    @FXML public Button buttonBoard;
    @FXML public IconButton buttonPrev;
    @FXML public IconButton buttonNext;
    private EngineManager engineManager;

    //TODO replace with thread
    private final Timeline updater = new Timeline(new KeyFrame(Duration.millis(100), event -> {
        engineManager.updateSimulationProgress(selectedSimulation.get());
        //selectedSimulation.get().setStatus(Status.valueOf(engineManager.engine.getSimulationStatus(selectedSimulation.get().getId()).getStatus()));
    }));

    private final ChangeListener<Status> simulationStatusListener = (observable, oldValue, newValue) -> {
        if (newValue != Status.RUNNING) {
            updater.stop();
            buttonPlayPause.setState(State.OFF);
            showSimulationResult(engineManager.engine.getSimulationResult(selectedSimulation.get().getId()));
            engineManager.updateSimulationProgress(selectedSimulation.get());
        } else {
            buttonPlayPause.setState(State.ON);
            updater.play();
        }
    };

    public ResultsController() {
        updater.setCycleCount(Timeline.INDEFINITE);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        listExecution.setItems(engineManager.getSimulationsList());
        selectedSimulation.addListener((observable, oldValue, newValue) -> {
            gridSeconds.setProgress(newValue.getProgressSeconds());
            gridTicks.setProgress(newValue.getProgressTicks());
            selectedStatus.unbind();
            selectedStatus.bind(newValue.statusProperty());
            showSimulationResult(engineManager.engine.getSimulationResult(newValue.getId()));
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
        }
        textResult.textProperty().set(result);
    }

    @FXML
    public void initialize() {
        selectedSimulation.bind(listExecution.getSelectionModel().selectedItemProperty());
        selectedStatus.addListener(simulationStatusListener);
        buttonPlayPause.setOnActionOn(this::actionSimulationResume);
        buttonPlayPause.setOnActionOff(this::actionSimulationPause);
        buttonStop.setOnAction(this::actionSimulationStop);
        buttonRerun.setOnAction(this::actionSimulationRerun);
        buttonBoard.setOnAction(this::actionShowBoard);
        buttonNext.setOnAction(this::actionSimulationNext);

        buttonPlayPause.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        buttonStop.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        buttonRerun.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED).not());
        buttonNext.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));

        listExecution.setCellFactory(new Callback<ListView<Simulation>, ListCell<Simulation>>() {
            @Override
            public ListCell<Simulation> call(ListView<Simulation> param) {
                return new ListCell<Simulation>() {
                    @Override
                    protected void updateItem(Simulation item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("Simulation #" + item.getId());
                        }
                    }

                    @Override
                    public void updateSelected(boolean selected) {
                        super.updateSelected(selected);

                        if(selected){
                            Animations.bounceRight(this);
                        }
                    }
                };
            }
        });

        updater.statusProperty().addListener((observable, oldValue, newValue) -> System.out.println("Updater is now " + newValue + " was " + oldValue));
    }

    private void actionSimulationPause(ActionEvent actionEvent) {
        if(selectedSimulation.get().getStatus() == Status.RUNNING) {
            engineManager.pauseSimulation(selectedSimulation.get().getId());
        }
    }

    private void actionSimulationStop(ActionEvent actionEvent) {
        if(selectedSimulation.get().getStatus() != Status.STOPPED) {
            engineManager.stopSimulation(selectedSimulation.get().getId());
        }
    }

    private void actionSimulationResume(ActionEvent actionEvent) {
        if(selectedSimulation.get().getStatus() == Status.PAUSED) {
            engineManager.resumeSimulation(selectedSimulation.get().getId());
        }
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
}
