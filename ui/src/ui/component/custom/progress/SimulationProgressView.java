package ui.component.custom.progress;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import ui.engine.Progress;

import java.io.IOException;

public class SimulationProgressView extends GridPane {

    @FXML private Label labelTitle;
    @FXML private Label labelValue;
    @FXML private Label labelMax;
    @FXML private ProgressBar progressBar;

    private final StringProperty title = new SimpleStringProperty();
    private final ObjectProperty<Progress> progress = new SimpleObjectProperty<>();

    public SimulationProgressView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/component/custom/progress/viewProgressSimulation.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setProgress(Progress progress) {
        this.progress.set(progress);
    }

    @FXML
    public void initialize(){
        labelTitle.textProperty().bind(title.concat(":"));
        Bindings.bindBidirectional(visibleProperty(), managedProperty());
        setVisible(false);
        progress.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                progressBar.progressProperty().bind(progress.get().percentageProperty());
                labelMax.textProperty().bind(progress.get().maxProperty().asString());
                labelValue.textProperty().bind(progress.get().valueProperty().asString());
                setVisible(progress.get().isEnabled());
            }
        });
    }
}
