package ui.subcomponent.execution.view;

import dto.detail.DTOEnvironmentVariable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.ToggleSwitch;
import validator.Validator;

import java.io.IOException;

public class EnvironmentVariableListCell extends ListCell<DTOEnvironmentVariable> {

    @FXML public Label labelTitle;
    @FXML public Slider sliderAmount;
    @FXML public TextField textFieldAmount;
    @FXML public Label labelMin;
    @FXML public Label labelMax;

    @FXML public ToggleSwitch toggleSwitchBoolean;


    @FXML public TextField textFieldString;
    @FXML public Label labelError;

    BooleanProperty validStringProperty = new SimpleBooleanProperty(true);

    private Parent layout;

    private boolean isNumericType;

    private boolean isBooleanType;

    private boolean isStringType;



    public EnvironmentVariableListCell() {
        super();
    }

    @FXML
    public void initialize(){
        if (isNumericType) {
            // Set up UI components and bindings for numeric type
            Bindings.bindBidirectional(textFieldAmount.textProperty(), sliderAmount.valueProperty(), new NumberStringConverter());
            labelMin.textProperty().bind(sliderAmount.minProperty().asString());
            labelMax.textProperty().bind(sliderAmount.maxProperty().asString());
        }
        else if(isBooleanType){
            // Listen to the selected property of the toggle switch
            toggleSwitchBoolean.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    toggleSwitchBoolean.setText("False");
                } else {
                    toggleSwitchBoolean.setText("True");
                }
            });
        }
        else if(isStringType) {
            // Bind the visibility property of the Error label to the negation of validStringProperty
            labelError.visibleProperty().bind(validStringProperty.not());

           /* // Optionally, you can set the initial visibility state based on the current value of validStringProperty
            labelError.setVisible(!validStringProperty.get());*/

            // Update validStringProperty based on validation
            textFieldString.textProperty().addListener((observable, oldValue, newValue) -> {
                validStringProperty.set(Validator.validate(newValue).isValidString().isValid());
            });
        }
    }

    @Override
    protected void updateItem(DTOEnvironmentVariable item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }
        isNumericType = "DECIMAL".equals(item.getType()) || "FLOAT".equals(item.getType());
        isBooleanType = "BOOLEAN".equals(item.getType());
        isStringType = "STRING".equals(item.getType());

        String fxmlFileName = getFxmlFileNameForType(item.getType());
        loadFxmlLayout(fxmlFileName);

        initialize(); // Call initialize here to set up UI for numeric type

        labelTitle.setText(item.getName());
        setGraphic(layout);
    }

    private String getFxmlFileNameForType(String type) {
        if (isBooleanType) {
            return "viewItemBoolean.fxml";
        } else if (isNumericType) {
            return "viewItemNumeric.fxml";
        } else if (isStringType) {
            return "viewItemString.fxml";
        } else {
            throw new IllegalArgumentException("Unknown environment variable type: " + type);
        }
    }

    private void loadFxmlLayout(String fxmlFileName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
        fxmlLoader.setController(this);
        try {
            layout = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}