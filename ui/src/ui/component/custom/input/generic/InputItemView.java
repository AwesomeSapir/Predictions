package ui.component.custom.input.generic;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import ui.style.Animations;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Random;

public abstract class InputItemView<T> extends GridPane {

    @FXML private Label labelTitle;

    @FXML protected Button buttonRandom;

    protected Random random = new Random();
    protected SimpleStringProperty title;
    protected SimpleBooleanProperty isValid;
    protected ObjectProperty<T> value;
    protected BooleanProperty randomizable;

    public InputItemView() {
        super();
        title = new SimpleStringProperty();
        isValid = new SimpleBooleanProperty(true);
        value = new SimpleObjectProperty<>();
        randomizable = new SimpleBooleanProperty(true);
    }

    protected abstract Collection<Node> getNodeToShake();

    protected void animateShake() {
        Animations.shake(buttonRandom);
        for (Node node : getNodeToShake()){
            Animations.shake(node);
        }
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

    public T getValue() {
        return value.get();
    }

    public abstract void setValue(Object value);

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public boolean isRandomizable() {
        return randomizable.get();
    }

    public BooleanProperty randomizableProperty() {
        return randomizable;
    }

    public void setRandomizable(boolean randomizable) {
        this.randomizable.set(randomizable);
    }

    public abstract void clear();

    protected void bind() {
        labelTitle.textProperty().bind(title);
        buttonRandom.setOnMouseClicked(this::clickRandom);
        buttonRandom.visibleProperty().bind(randomizable);
        buttonRandom.managedProperty().bind(randomizable);
    }

    private void clickRandom(MouseEvent event){
        randomize();
        animateShake();
    }

    protected abstract void randomize();

    @FXML
    public void initialize(){
        bind();
        clear();
    }
}
