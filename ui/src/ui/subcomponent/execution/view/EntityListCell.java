package ui.subcomponent.execution.view;

import dto.detail.DTOEntity;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;

public class EntityListCell extends ListCell<DTOEntity> {

    @FXML public Label labelTitle;
    @FXML public Slider sliderAmount;
    @FXML public TextField textFieldAmount;
    @FXML public Label labelMin;
    @FXML public Label labelMax;

    @FXML public Parent layout;

    public EntityListCell() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewItemNumeric.fxml"));
        fxmlLoader.setController(this);
        try {
            layout = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize(){
        Bindings.bindBidirectional(textFieldAmount.textProperty(), sliderAmount.valueProperty(), new NumberStringConverter());
        labelMin.textProperty().bind(sliderAmount.minProperty().asString());
        labelMax.textProperty().bind(sliderAmount.maxProperty().asString());
    }

    @Override
    protected void updateItem(DTOEntity item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null || item.getName() == null) {
            labelTitle.setText(null);
            setGraphic(null);
            return;
        }

        labelTitle.setText(item.getName());
        setGraphic(layout);
    }
}
