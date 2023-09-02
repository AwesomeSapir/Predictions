package ui.subcomponent.result;

import dto.simulation.DTOSimulationResult;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import ui.engine.EngineManager;

public class ResultsController {
    @FXML public TextArea textResult;
    @FXML public ListView<DTOSimulationResult> listExecution;

    private EngineManager engineManager;

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        listExecution.setItems(engineManager.getSimulations());
        listExecution.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSimulationResult(newValue);
        });
    }

    public void setSimulationResult(DTOSimulationResult simulationResult) {
        String result = "Simulation result:\n" + "Id: " + simulationResult.getId() + "\nTermination reason:";
        if (simulationResult.isBySeconds()) {
            result += " by seconds\n";
        }
        if (simulationResult.isByTicks()) {
            result += " by ticks\n";
        }
        textResult.textProperty().set(result);
    }

    @FXML
    public void initialize(){
        listExecution.setCellFactory(new Callback<ListView<DTOSimulationResult>, ListCell<DTOSimulationResult>>() {
            @Override
            public ListCell<DTOSimulationResult> call(ListView<DTOSimulationResult> param) {
                return new ListCell<DTOSimulationResult>() {
                    @Override
                    protected void updateItem(DTOSimulationResult item, boolean empty) {
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
