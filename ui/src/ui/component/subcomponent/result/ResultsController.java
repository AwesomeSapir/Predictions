package ui.component.subcomponent.result;

import dto.simulation.DTOEntityPopulation;
import dto.simulation.DTOSimulationResult;
import dto.simulation.DTOStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Duration;
import ui.engine.EngineManager;
import ui.engine.Simulation;

public class ResultsController {
    @FXML public TextArea textResult;
    @FXML public ListView<Simulation> listExecution;
    @FXML public GridPane gridTicks;
    @FXML public GridPane gridSeconds;
    @FXML public ProgressBar progressBarSeconds;
    @FXML public ProgressBar progressBarTicks;

    private final ObjectProperty<Simulation> selectedSimulation = new SimpleObjectProperty<>();

    private EngineManager engineManager;

    private final ChangeListener<Boolean> simulationResultListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                setSimulationResult(selectedSimulation.get().getResult());
                updateSimulation(selectedSimulation.get());
                timeline.stop();
            }
        }
    };

    private final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), event -> updateSimulation(selectedSimulation.get())));

    public ResultsController() {
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        listExecution.setItems(engineManager.getSimulations());
        selectedSimulation.addListener((observable, oldValue, newValue) -> {
            progressBarSeconds.progressProperty().bind(newValue.getProgressSeconds().percentageProperty());
            progressBarTicks.progressProperty().bind(newValue.getProgressTicks().percentageProperty());
            gridSeconds.setVisible(newValue.getProgressSeconds().isEnabled());
            gridTicks.setVisible(newValue.getProgressTicks().isEnabled());

            setSimulationResult(newValue.getResult());

            updateSimulation(newValue);

            if (!newValue.isResultReady()) {
                if (oldValue != null) {
                    oldValue.resultReadyProperty().removeListener(simulationResultListener);
                }
                newValue.resultReadyProperty().addListener(simulationResultListener);
                timeline.play();
            } else {
                timeline.stop();
            }
        });
    }

    public void updateSimulation(Simulation simulation){
        DTOStatus status = engineManager.engine.getSimulationStatus(simulation.getId());
        simulation.getProgressSeconds().setValue(status.getSeconds());
        simulation.getProgressTicks().setValue(status.getTicks());
    }

    public void setSimulationResult(DTOSimulationResult simulationResult) {
        String result;
        if(simulationResult == null){
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

    private void bindVisibility(Node... nodes){
        for (Node node : nodes){
            Bindings.bindBidirectional(node.visibleProperty(), node.managedProperty());
        }
    }

    @FXML
    public void initialize(){
        bindVisibility(gridSeconds, gridTicks);

        selectedSimulation.bind(listExecution.getSelectionModel().selectedItemProperty());

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
}
