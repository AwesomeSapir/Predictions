package ui.component.subcomponent.result;

import dto.simulation.DTOEntityPopulation;
import dto.simulation.DTOSimulationResult;
import dto.simulation.DTOStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Duration;
import ui.component.custom.progress.SimulationProgressView;
import ui.engine.EngineManager;
import ui.engine.Progress;
import ui.engine.Simulation;
import ui.engine.Status;

public class ResultsController {
    @FXML
    public TextArea textResult;
    @FXML
    public ListView<Simulation> listExecution;
    @FXML
    public SimulationProgressView gridSeconds;
    @FXML
    public SimulationProgressView gridTicks;
    @FXML
    public Button buttonResume;
    @FXML
    public Button buttonPause;
    @FXML
    public Button buttonStop;

    private final ObjectProperty<Simulation> selectedSimulation = new SimpleObjectProperty<>();
    private final ObjectProperty<Status> selectedStatus = new SimpleObjectProperty<>();
    @FXML
    public ScrollPane paneDetails;
    private EngineManager engineManager;

    private final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
        updateSimulation(selectedSimulation.get());
        System.out.println("timeline is running");
    }));

    private final ChangeListener<Boolean> simulationResultListener = (observable, oldValue, newValue) -> {
        if (newValue) {
            setSimulationResult(selectedSimulation.get().getResult());
            updateSimulation(selectedSimulation.get());
            timeline.stop();
        }
    };

    private final ChangeListener<Status> simulationStatusListener = (observable, oldValue, newValue) -> {
        bindSimulationControls(newValue);
        if (newValue != Status.RUNNING) {
            timeline.stop();
            setSimulationResult(selectedSimulation.get().getResult());
        } else {
            timeline.play();
        }
    };

    public ResultsController() {
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        listExecution.setItems(engineManager.getSimulationsList());
        selectedSimulation.addListener((observable, oldValue, newValue) -> {
            gridSeconds.setProgress(newValue.getProgressSeconds());
            gridTicks.setProgress(newValue.getProgressTicks());
            selectedStatus.unbind();
            selectedStatus.bind(newValue.statusProperty());
            /*updateSimulation(newValue);

            if (!newValue.isResultReady()) {
                if (oldValue != null) {
                    oldValue.resultReadyProperty().removeListener(simulationResultListener);
                }
                newValue.resultReadyProperty().addListener(simulationResultListener);
                timeline.play();
            } else {
                timeline.stop();
            }*/
        });
    }

    public void bindSimulationControls(Status status) {
        switch (status) {
            case RUNNING:
                buttonPause.setDisable(false);
                buttonResume.setDisable(true);
                buttonStop.setDisable(false);
                timeline.play();
                break;
            case STOPPED:
                buttonPause.setDisable(true);
                buttonResume.setDisable(true);
                buttonStop.setDisable(true);
                timeline.stop();
                break;
            case PAUSED:
                buttonPause.setDisable(true);
                buttonResume.setDisable(false);
                buttonStop.setDisable(false);
                timeline.stop();
                break;
        }
    }

    public void updateSimulation(Simulation simulation) {
        DTOStatus status = engineManager.engine.getSimulationStatus(simulation.getId());
        simulation.getProgressSeconds().setValue(status.getSeconds());
        simulation.getProgressTicks().setValue(status.getTicks());
    }

    public void setSimulationResult(DTOSimulationResult simulationResult) {
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
        buttonPause.setOnAction(this::actionSimulationPause);
        buttonResume.setOnAction(this::actionSimulationResume);
        buttonStop.setOnAction(this::actionSimulationStop);

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
                };
            }
        });
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
}
