package ui.component.subcomponent.result;

import dto.detail.DTOProperty;
import dto.simulation.DTOEntityPopulation;
import dto.simulation.DTOSimulationHistogram;
import dto.simulation.DTOSimulationResult;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import ui.component.custom.board.BoardView;
import ui.component.custom.progress.SimulationProgressView;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.engine.Status;
import ui.style.StyleManager;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsController {
    @FXML public TextArea textResult;
    @FXML public ListView<Simulation> listExecution;
    @FXML public SimulationProgressView gridSeconds;
    @FXML public SimulationProgressView gridTicks;
    @FXML public Button buttonResume;
    @FXML public Button buttonPause;
    @FXML public Button buttonStop;
    @FXML public Button buttonRerun;
    @FXML public ScrollPane paneDetails;

    private final ObjectProperty<Simulation> selectedSimulation = new SimpleObjectProperty<>();
    private final ObjectProperty<Status> selectedStatus = new SimpleObjectProperty<>();
    @FXML public Button buttonBoard;
    @FXML public Button buttonNext;
    @FXML public LineChart entityAmountByTicks;
    @FXML public ComboBox propertyComboBox;
    @FXML public ComboBox entityComboBox;
    @FXML public TabPane simulationResultsMainTanPane;
    @FXML public Tab chartLineTab;
    @FXML public Tab entityPropertyTab;
    @FXML public ComboBox propertyDataDisplayComboBox;
    @FXML public VBox histogramPlaceholder;

    @FXML private TableView<EntityInfo> entityTable;

    @FXML private TableColumn<EntityInfo, String> entityNameColumn;

    @FXML private TableColumn<EntityInfo, Integer> instanceCountColumn;

    //Preparing the data points for the line1
    private Map<Integer, Map<String,XYChart.Series>> seriesMap = new HashMap<>();



    private EngineManager engineManager;

    //TODO replace with thread
    private final Timeline updater = new Timeline(new KeyFrame(Duration.millis(100), event -> {
        engineManager.updateSimulationProgress(selectedSimulation.get());
        populateEntityTable(selectedSimulation.get().getId());
        //selectedSimulation.get().setStatus(Status.valueOf(engineManager.engine.getSimulationStatus(selectedSimulation.get().getId()).getStatus()));
    }));

    private final ChangeListener<Status> simulationStatusListener = (observable, oldValue, newValue) -> {
        if (newValue != Status.RUNNING) {
            updater.stop();
            showSimulationResult(engineManager.engine.getSimulationResult(selectedSimulation.get().getId()));
            engineManager.updateSimulationProgress(selectedSimulation.get());
        } else {
            updater.play();
        }
    };

    public ResultsController() {
        updater.setCycleCount(Timeline.INDEFINITE);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        listExecution.setItems(engineManager.getSimulationsList());
        entityAmountByTicks.setTitle("Quantity as a function of ticks");
        selectedSimulation.addListener((observable, oldValue, newValue) -> {
            gridSeconds.setProgress(newValue.getProgressSeconds());
            gridTicks.setProgress(newValue.getProgressTicks());
            entityAmountByTicks.visibleProperty().unbind();
            entityAmountByTicks.setVisible(false);
            selectedStatus.unbind();
            entityComboBox.visibleProperty().unbind();
            propertyComboBox.visibleProperty().unbind();
            propertyDataDisplayComboBox.visibleProperty().unbind();
            entityComboBox.setVisible(false);
            propertyComboBox.setVisible(false);
            propertyDataDisplayComboBox.setVisible(false);
            selectedStatus.bind(newValue.statusProperty());
            entityAmountByTicks.getData().clear();
            entityComboBox.getItems().clear();
            propertyComboBox.getItems().clear();
            histogramPlaceholder.getChildren().clear(); // Clear any previous content
            propertyDataDisplayComboBox.getItems().clear();
            showSimulationResult(engineManager.engine.getSimulationResult(newValue.getId()));
            propertyDataDisplayComboBox.getItems().add(0,"Histogram of population");
            propertyDataDisplayComboBox.getItems().add(1,"Consistency");
            propertyDataDisplayComboBox.getItems().add(2,"Average value");
            entityAmountByTicks.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
            entityComboBox.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
            propertyComboBox.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
            propertyDataDisplayComboBox.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
            populateEntityTable(newValue.getId());
        });
    }

    public void showSimulationResult(DTOSimulationResult simulationResult) {
        String result;
        if (simulationResult == null) {
            result = "Simulation is still running...";
        } else {
            result = "Simulation result:\n" + "Id: " + simulationResult.getId() + "\nTermination reason:";
            if (simulationResult.isBySeconds()) {
                result += " by seconds\n";
            }
            if (simulationResult.isByTicks()) {
                result += " by ticks\n";
            }

            for (DTOEntityPopulation entityPopulation : engineManager.engine.getDetailsByEntityCount(simulationResult.getId())) {
                result += "\nEntity Name: " + entityPopulation.getEntity().getName();
                result += "\nInitial Quantity: " + entityPopulation.getInitialPopulation();
                result += "\nFinal Quantity: " + entityPopulation.getFinalPopulation();
                // Populate characteristicComboBox with available characteristics
                // Call the updatePropertyComboBox method with the simulation ID and selected entity
                entityComboBox.getItems().add(entityPopulation.getEntity().getName());
            }
            // Get or create the series for the current simulation ID
            Map<String, XYChart.Series> simulationSeriesMap = seriesMap.get(simulationResult.getId());
            if (simulationSeriesMap != null) {
                for (XYChart.Series series : simulationSeriesMap.values()) {
                    entityAmountByTicks.getData().add(series);
                }
            }
        }
        textResult.textProperty().set(result);
    }


    @FXML
    public void initialize() {
        selectedSimulation.bind(listExecution.getSelectionModel().selectedItemProperty());
        selectedStatus.addListener(simulationStatusListener);
        entityAmountByTicks.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        entityComboBox.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        propertyComboBox.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        buttonPause.setOnAction(this::actionSimulationPause);
        buttonResume.setOnAction(this::actionSimulationResume);
        buttonStop.setOnAction(this::actionSimulationStop);
        buttonRerun.setOnAction(this::actionSimulationRerun);
        buttonBoard.setOnAction(this::actionShowBoard);
        buttonNext.setOnAction(this::actionSimulationNext);

        buttonPause.disableProperty().bind(selectedStatus.isEqualTo(Status.RUNNING).not());
        buttonResume.disableProperty().bind(selectedStatus.isEqualTo(Status.PAUSED).not());
        buttonStop.disableProperty().bind(Bindings.and(
                selectedStatus.isNotEqualTo(Status.RUNNING),
                selectedStatus.isNotEqualTo(Status.PAUSED)));
        buttonRerun.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED).not());
        buttonNext.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));

        listExecution.setCellFactory(new Callback<ListView<Simulation>, ListCell<Simulation>>() {
            @Override
            public ListCell<Simulation> call(ListView<Simulation> param) {
                return new ListCell<Simulation>() {
                    @Override
                    protected void updateItem(Simulation item, boolean empty) {
                        String runDate = "";
                        if(item != null) {
                            runDate = item.getRunDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss"));
                        }
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("Simulation #" + item.getId() + "   " + runDate);
                        }
                    }
                };
            }
        });

        // Bind the columns to the EntityInfo properties
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        instanceCountColumn.setCellValueFactory(new PropertyValueFactory<>("instanceCount"));

        updater.statusProperty().addListener((observable, oldValue, newValue) -> System.out.println("Updater is now " + newValue + " was " + oldValue));
        // Add a listener to the entityComboBox selection
         entityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected entity
                String selectedEntity = newValue.toString();
                // Clear and populate the propertyComboBox with entity properties
                propertyComboBox.getItems().clear();
                histogramPlaceholder.getChildren().clear(); // Clear any previous content

                // Call the updatePropertyComboBox method with the simulation ID and selected entity
                if (selectedSimulation.get() != null) {
                    int simulationId = selectedSimulation.get().getId();
                    updatePropertyComboBox(simulationId, selectedEntity);
                }
            }
        });

        propertyComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected entity
                String selectedProperty = newValue.toString();
                String selectedEntity = (String) entityComboBox.getSelectionModel().getSelectedItem();
                String selectedDisplayWay = (String) propertyDataDisplayComboBox.getSelectionModel().getSelectedItem();

                // Call the updatePropertyComboBox method with the simulation ID and selected entity
                if (selectedSimulation.get() != null) {
                    int simulationId = selectedSimulation.get().getId();
                    if(selectedEntity != null && selectedDisplayWay != null) {
                        updatePropertyDataDisplayComboBox(simulationId, selectedEntity, selectedProperty, selectedDisplayWay);
                    }
                }
            }
        });

        propertyDataDisplayComboBox.getItems().add(0,"Histogram of population");
        propertyDataDisplayComboBox.getItems().add(1,"Consistency");
        propertyDataDisplayComboBox.getItems().add(2,"Average value");
        propertyDataDisplayComboBox.visibleProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));

        propertyDataDisplayComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected entity
                String selectedDisplayWay = newValue.toString();

                // Call the updatePropertyComboBox method with the simulation ID and selected entity
                if (selectedSimulation.get() != null) {
                    int simulationId = selectedSimulation.get().getId();
                    String selectedEntity = (String) entityComboBox.getSelectionModel().getSelectedItem();
                    String selectedProperty = (String) propertyComboBox.getSelectionModel().getSelectedItem();
                    if(selectedEntity != null && selectedProperty != null) {
                        updatePropertyDataDisplayComboBox(simulationId, selectedEntity, selectedProperty, selectedDisplayWay);
                    }
                }
            }
        });
    }

    private void updatePropertyComboBox(int simulationId, String selectedEntity) {
        // Get entity properties for the selected entity using simulationId
        List<DTOProperty> entityProperties = (List<DTOProperty>) engineManager.engine.getPastEntityProperties(simulationId,selectedEntity);

        // Clear and populate the propertyComboBox with entity properties
        propertyComboBox.getItems().clear();
        for (DTOProperty property : entityProperties) {
            propertyComboBox.getItems().add(property.getName());
        }
    }

    private void updatePropertyDataDisplayComboBox(int simulationId,String selectedEntity,String selectedProperty, String selectedDisplayWay) {
        histogramPlaceholder.getChildren().clear(); // Clear any previous content
       switch (selectedDisplayWay){
           case "Histogram of population":
               displayHistogram(simulationId,selectedEntity,selectedProperty);
               break;
           case "Consistency":
               displayConsistency(simulationId,selectedEntity,selectedProperty);
               break;
           case "Average value":
               displayAverageValue(simulationId,selectedEntity,selectedProperty);
           default:
       }
    }

    private void displayHistogram(int simulationId,String selectedEntity,String selectedProperty){
        // Get the histogram data for the selected property
        DTOSimulationHistogram histogram = engineManager.engine.getValuesForPropertyHistogram(simulationId, selectedProperty,selectedEntity);

        // Create a CategoryAxis for the property values and a NumberAxis for the counts
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create the bar chart
        BarChart<String, Number> histogramChart = new BarChart<>(xAxis, yAxis);
        histogramChart.setTitle("Histogram for " + selectedProperty);

        // Create a series for the histogram data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(selectedProperty);

        // Populate the series with data from the histogram
        for (Map.Entry<Object, Integer> entry : histogram.getValueToCount().entrySet()) {
            String valueAsString = entry.getKey().toString();
            series.getData().add(new XYChart.Data<>(valueAsString, entry.getValue()));
        }

        // Add the series to the chart
        histogramChart.getData().add(series);

        // Add the BarChart to the histogramPlaceholder VBox
        VBox histogramPlaceholder = (VBox) simulationResultsMainTanPane.getTabs().get(1).getContent().lookup("#histogramPlaceholder");
        histogramPlaceholder.getChildren().add(histogramChart);
    }

    private void displayConsistency(int simulationId,String selectedEntity,String selectedProperty){

    }

    private void displayAverageValue(int simulationId,String selectedEntity,String selectedProperty){

    }
    private void actionSimulationPause(ActionEvent actionEvent) {
        engineManager.pauseSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationStop(ActionEvent actionEvent) {
        engineManager.stopSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationResume(ActionEvent actionEvent) {
        engineManager.resumeSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationRerun(ActionEvent actionEvent){

    }

    private void actionShowBoard(ActionEvent actionEvent){
        Stage stage = new Stage();
        BoardView boardView = new BoardView(engineManager, selectedSimulation.get());
        boardView.setSize(100, 100);
        Scene scene = new Scene(boardView);
        StyleManager.register(scene);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            boardView.stop();
            StyleManager.unregister(scene);
        });
        stage.show();
    }

    private void actionSimulationNext(ActionEvent actionEvent){
        engineManager.tickSimulation(selectedSimulation.get().getId());
        engineManager.updateSimulationProgress(selectedSimulation.get());
    }

    private void populateEntityTable(int simulationId) {
        // Clear existing items in the table and reset the series
        entityTable.getItems().clear();

        // Get entity information from the engineManager
        Collection<DTOEntityPopulation> entityPopulations = engineManager.engine.getDetailsByEntityCount(simulationId);

        // Populate the table with entity information
        for (DTOEntityPopulation entityPopulation : entityPopulations) {
            String entityName = entityPopulation.getEntity().getName();
            Map<String, XYChart.Series> stringSeriesMap = seriesMap.computeIfAbsent(simulationId, k -> new HashMap<>());
            XYChart.Series series = stringSeriesMap.computeIfAbsent(entityName, k -> new XYChart.Series());
            series.setName(entityName); // Set the name for the series

            int population = entityPopulation.getFinalPopulation();
            double ticks = engineManager.getSimulations().get(simulationId).getProgressTicks().getValue();
            entityTable.getItems().add(new EntityInfo(entityName, population));

            if (ticks % 10 == 0) {
                // Add data points to the LineChart series
                series.getData().add(new XYChart.Data<>(ticks, population));
                stringSeriesMap.put(entityName,series);
                seriesMap.put(simulationId, stringSeriesMap);
            }
        }
    }
}
