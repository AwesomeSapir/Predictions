package ui.customcomponent.view.item;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public abstract class InputItemView extends GridPane {

    @FXML private Label labelTitle;

    protected SimpleStringProperty title;

    public InputItemView(String fxmlPath) {
        super();
        title = new SimpleStringProperty();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    @FXML
    public void initialize(){
        labelTitle.textProperty().bind(title);
    }
}
