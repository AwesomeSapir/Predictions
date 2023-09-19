package ui.component.subcomponent.result;

import dto.detail.DTOProperty;
import dto.simulation.DTOSimulationHistogram;
import dto.simulation.DTOSimulationResult;
import javafx.beans.binding.Bindings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import ui.component.custom.board.BoardView;
import ui.component.custom.node.IconButton;
import ui.component.custom.node.ToggleIconButton;
import ui.component.custom.node.State;
import ui.component.custom.progress.SimulationProgressView;
import ui.engine.EngineManager;
import ui.engine.EntityInfo;
import ui.engine.Simulation;
import ui.engine.Status;
import ui.style.Animations;
import ui.style.StyleManager;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsController {
    @FXML public ListView<Simulation> listExecution;
    @FXML public ToggleIconButton buttonPlayPause;
    @FXML public SimulationProgressView gridSeconds, gridTicks;
    @FXML public Button buttonStop, buttonRerun, buttonBoard, buttonPrev, buttonNext;
    @FXML Label labelStatus, labelTermination;
    @FXML public ScrollPane paneDetails, paneResult;

    private final ObjectProperty<Simulation> selectedSimulation = new SimpleObjectProperty<>();
    private final ObjectProperty<Status> selectedStatus = new SimpleObjectProperty<>();

    private Map<Integer, Map<String, XYChart.Series>> seriesMap = new HashMap<>();
    @FXML public LineChart<Integer, Integer> chartPopulationPerTick;

    @FXML public ComboBox<DTOProperty> comboBoxProperty; //TODO Change to PropertyInfo
    @FXML public ComboBox<EntityInfo> comboBoxEntity;
    @FXML public ComboBox<DisplayType> comboBoxDisplayType;

    @FXML
    public TabPane simulationResultsMainTabPane;
    @FXML
    public Tab chartLineTab, entityPropertyTab;
    @FXML
    public VBox vboxDisplayType;

    @FXML
    private TableView<EntityInfo> entityTable;
    @FXML
    private TableColumn<EntityInfo, String> entityNameColumn;
    @FXML
    private TableColumn<EntityInfo, Integer> instanceCountColumn;

    private EngineManager engineManager;

    private final ChangeListener<Status> simulationStatusListener = (observable, oldValue, newValue) -> {
        if (newValue != Status.RUNNING) {
            buttonPlayPause.setState(State.OFF);
            showSimulationResult(engineManager.getSimulationResult(selectedSimulation.get().getId()));
            updateDisplayData();
        } else {
            buttonPlayPause.setState(State.ON);
        }
    };

    private final ChangeListener<Number> simulationTicksListener = (observable, oldValue, newValue) -> updateDisplayData();

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        listExecution.setItems(engineManager.getSimulationsList());
        chartPopulationPerTick.setTitle("Quantity as a function of ticks");
        selectedSimulation.addListener((observable, oldValue, newValue) -> {
            showSimulationResult(engineManager.getSimulationResult(newValue.getId()));
            gridSeconds.setProgress(newValue.getProgressSeconds());
            gridTicks.setProgress(newValue.getProgressTicks());

            selectedStatus.unbind();
            selectedStatus.bind(newValue.statusProperty());

            comboBoxEntity.setItems(selectedSimulation.get().getEntities());
            comboBoxProperty.getItems().clear();
            vboxDisplayType.getChildren().clear(); // Clear any previous content

            chartPopulationPerTick.setData(selectedSimulation.get().getEntityPopulationSeriesList());

            if (oldValue != null) {
                oldValue.getProgressTicks().valueProperty().removeListener(simulationTicksListener);
            }
            newValue.getProgressTicks().valueProperty().addListener(simulationTicksListener);

            entityTable.setItems(newValue.getEntities());
        });
    }

    public void showSimulationResult(DTOSimulationResult simulationResult) {
        String result;
        if (simulationResult == null || simulationResult.getTerminationReason() == null) {
            result = "Simulation is still running...";
        } else {
            result = simulationResult.getTerminationReason().getName().toLowerCase();
        }
        labelTermination.textProperty().set(result);

    }

    @FXML
    public void initialize() {
        selectedSimulation.bind(listExecution.getSelectionModel().selectedItemProperty());
        selectedStatus.addListener(simulationStatusListener);
        buttonPlayPause.setOnActionOn(this::actionSimulationResume);
        buttonPlayPause.setOnActionOff(this::actionSimulationPause);
        buttonStop.setOnAction(this::actionSimulationStop);
        buttonRerun.setOnAction(this::actionSimulationRerun);
        buttonBoard.setOnAction(this::actionShowBoard);
        buttonNext.setOnAction(this::actionSimulationNext);
        labelStatus.textProperty().bind(selectedStatus.asString());

        comboBoxDisplayType.getItems().setAll(DisplayType.values());

        comboBoxEntity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            comboBoxProperty.getSelectionModel().clearSelection();
            if (selectedSimulation.get() != null) {
                int simulationId = selectedSimulation.get().getId();
                updatePropertyComboBox(simulationId, newValue);
            }
        });

        comboBoxProperty.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDisplayData();
        });

        comboBoxDisplayType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDisplayData();
        });

        buttonPlayPause.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        buttonStop.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED));
        buttonRerun.disableProperty().bind(selectedStatus.isEqualTo(Status.STOPPED).not());
        buttonNext.disableProperty().bind(selectedStatus.isEqualTo(Status.PAUSED).not());

        listExecution.setCellFactory(new Callback<ListView<Simulation>, ListCell<Simulation>>() {
            @Override
            public ListCell<Simulation> call(ListView<Simulation> param) {
                return new ListCell<Simulation>() {
                    @Override
                    protected void updateItem(Simulation item, boolean empty) {
                        String runDate = "";
                        if (item != null) {
                            runDate = item.getRunDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss"));
                        }
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("Simulation #" + item.getId() + "   " + runDate);
                        }
                    }

                    @Override
                    public void updateSelected(boolean selected) {
                        super.updateSelected(selected);

                        if(selected){
                            Animations.bounceRight(this);
                        }
                    }
                };
            }
        });

        // Bind the columns to the EntityInfo properties
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        instanceCountColumn.setCellValueFactory(new PropertyValueFactory<>("instanceCount"));
    }

    //TODO simulation should hold the properties
    private void updatePropertyComboBox(int simulationId, EntityInfo selectedEntity) {
        // Get entity properties for the selected entity using simulationId
        List<DTOProperty> entityProperties = (List<DTOProperty>) engineManager.getPastEntityProperties(simulationId, selectedEntity.getEntityName());

        // Clear and populate the propertyComboBox with entity properties
        comboBoxProperty.getItems().clear();
        for (DTOProperty property : entityProperties) {
            comboBoxProperty.getItems().add(property);
        }
    }

    private void updateDisplayData() {
        vboxDisplayType.getChildren().clear(); // Clear any previous content
        int id = selectedSimulation.get().getId();
        if(selectedSimulation.get().getStatus() != Status.RUNNING) {
            DisplayType type = comboBoxDisplayType.getSelectionModel().getSelectedItem();
            EntityInfo entity = comboBoxEntity.getSelectionModel().getSelectedItem();
            DTOProperty property = comboBoxProperty.getSelectionModel().getSelectedItem();
            if (type != null && entity != null && property != null) {
                switch (type) {
                    case Histogram:
                        displayHistogram(id, entity.getEntityName(), property.getName());
                        break;
                    case Consistency:
                        displayConsistency(id, entity.getEntityName(), property.getName());
                        break;
                    case Average:
                        displayAverageValue(id, entity.getEntityName(), property.getName());
                        break;
                }
            }
        }
    }

    private void displayHistogram(int simulationId, String selectedEntity, String selectedProperty) {
        // Get the histogram data for the selected property
        DTOSimulationHistogram histogram = engineManager.getValuesForPropertyHistogram(simulationId, selectedEntity, selectedProperty);

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

        vboxDisplayType.getChildren().add(histogramChart);
    }

    private void displayConsistency(int simulationId, String selectedEntity, String selectedProperty) {

        // Get the list of ticksOfSameValue for the selected property
        List<Double> listOfTicksOfSameValue = (List<Double>) engineManager.getConsistencyOfProperty(simulationId, selectedEntity, selectedProperty);

        // Calculate the average consistency
        double averageConsistency = calculateAverage(listOfTicksOfSameValue);

        // Display the average consistency
        String consistencyText = "Average Consistency for '" + selectedProperty + "': " + averageConsistency;
        Label titleLabel = new Label(consistencyText);

        vboxDisplayType.getChildren().add(titleLabel);
    }

    private void displayAverageValue(int simulationId, String selectedEntity, String selectedProperty) {
        // Get the histogram data for the selected property
        DTOSimulationHistogram histogram = engineManager.getValuesForPropertyHistogram(simulationId, selectedEntity, selectedProperty);
        // Populate the series with data from the histogram
        for (Map.Entry<Object, Integer> entry : histogram.getValueToCount().entrySet()) {
            Object verifyNumericValue = entry.getKey();
            if (!(verifyNumericValue instanceof Double) && !(verifyNumericValue instanceof Integer)) {
                // Display the average consistency
                String consistencyText = "The property '" + selectedProperty + "' is not numeric";
                Label titleLabel = new Label(consistencyText);
                vboxDisplayType.getChildren().add(titleLabel);
                return;
            }
            break;
        }
        List<Double> listNumericValues = new ArrayList<>();
        // Populate the series with data from the histogram
        for (Map.Entry<Object, Integer> entry : histogram.getValueToCount().entrySet()) {
            Double value = Double.valueOf(entry.getKey().toString());
            listNumericValues.add(value);
        }
        // Calculate the average value
        double averageValue = calculateAverage(listNumericValues);

        String consistencyText = "Average Value for '" + selectedProperty + "': " + averageValue;
        Label titleLabel = new Label(consistencyText);
        vboxDisplayType.getChildren().add(titleLabel);
    }

    private double calculateAverage(List<Double> listOfValues) {
        if (listOfValues.isEmpty()) {
            return 0.0;
        }

        double totalConsistency = 0;
        int valueCount = listOfValues.size();

        for (Double consistency : listOfValues) {
            totalConsistency += consistency;
        }
        return totalConsistency / valueCount;
    }

    private void actionSimulationPause(ActionEvent actionEvent) {
        if(selectedSimulation.get().getStatus() == Status.RUNNING) {
            engineManager.pauseSimulation(selectedSimulation.get().getId());
        }
    }

    private void actionSimulationStop(ActionEvent actionEvent) {
        if(selectedSimulation.get().getStatus() != Status.STOPPED) {
            engineManager.stopSimulation(selectedSimulation.get().getId());
        }
    }

    private void actionSimulationResume(ActionEvent actionEvent) {
        if(selectedSimulation.get().getStatus() == Status.PAUSED) {
            engineManager.resumeSimulation(selectedSimulation.get().getId());
        }
    }

    private void actionSimulationRerun(ActionEvent actionEvent) {

    }

    private void actionShowBoard(ActionEvent actionEvent) {
        Stage stage = new Stage();
        BoardView boardView = new BoardView(engineManager, selectedSimulation.get());
        Scene scene = new Scene(boardView);
        StyleManager.register(scene);
        stage.setScene(scene);
        stage.setTitle("Simulation #" + selectedSimulation.get().getId() + " Grid");
        stage.setOnCloseRequest(event -> {
            boardView.stop();
            StyleManager.unregister(scene);
        });
        stage.show();
    }

    private void actionSimulationNext(ActionEvent actionEvent) {
        selectedSimulation.get().setStatus(Status.RUNNING);
        engineManager.tickSimulation(selectedSimulation.get().getId());
    }
}
