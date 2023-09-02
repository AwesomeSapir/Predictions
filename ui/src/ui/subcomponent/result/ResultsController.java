package ui.subcomponent.result;

import dto.simulation.DTOSimulationResult;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ResultsController {
    @FXML public TextArea textResult;
    @FXML public ListView listExecution;

    private DTOSimulationResult simulationResult;

    public void setSimulationResult(DTOSimulationResult simulationResult) {
        this.simulationResult = simulationResult;
        String result = "Simulation result:\n" + "Id: " + simulationResult.getId() + "\nTermination reason:";
        if (simulationResult.isBySeconds()) {
            result += " by seconds\n";
        }
        if (simulationResult.isByTicks()) {
            result += " by ticks\n";
        }
        textResult.textProperty().set(result);
    }
}
