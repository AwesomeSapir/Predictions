package ui.component.subcomponent.queue;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ui.engine.Queue;

public class QueueController {

    private Queue queue;
    @FXML public Label labelActive;
    @FXML public Label labelQueue;
    @FXML public Label labelPaused;
    @FXML public Label labelFinished;
    @FXML public Label labelRunning;

    public void setQueue(Queue queue) {
        this.queue = queue;
        labelActive.textProperty().bind(queue.activeProperty().asString());
        labelQueue.textProperty().bind(queue.runningProperty().subtract(queue.activeProperty()).asString());
        labelPaused.textProperty().bind(queue.pausedProperty().asString());
        labelFinished.textProperty().bind(queue.stoppedProperty().asString());
        labelRunning.textProperty().bind(queue.runningProperty().asString());
    }

    @FXML
    public void initialize(){

    }
}
