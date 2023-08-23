package ui.main;

import engine.Engine;
import engine.EngineInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import ui.subcomponent.detail.DetailsController;

import java.io.File;

public class MainController {

    EngineInterface engine = new Engine();

    @FXML private BorderPane mainBorderPane;
    @FXML private Button chooseFileButton;
    @FXML private TextField filePathTextField;
    @FXML private DetailsController tabDetailsController;

    @FXML
    void chooseXMLFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());
        filePathTextField.setText(selectedFile.getAbsolutePath());
        engine.loadXml(selectedFile.getAbsolutePath());
    }
}
