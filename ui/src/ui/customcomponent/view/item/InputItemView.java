package ui.customcomponent.view.item;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

public abstract class InputItemView<T> extends GridPane {

    @FXML private Label labelTitle;

    protected SimpleStringProperty title;
    protected SimpleBooleanProperty isValid;
    protected ObjectProperty<T> value;

    public InputItemView() {
        super();
        title = new SimpleStringProperty();
        isValid = new SimpleBooleanProperty(true);
        value = new SimpleObjectProperty<>();
    }

    protected void load(URL fxmlPath){
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlPath);
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

    public boolean isValid() {
        return isValid.get();
    }

    public SimpleBooleanProperty isValidProperty() {
        return isValid;
    }

    public Object getValue() {
        return value.get();
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public abstract void clear();

    @FXML
    public void initialize(){
        labelTitle.textProperty().bind(title);
    }
}
