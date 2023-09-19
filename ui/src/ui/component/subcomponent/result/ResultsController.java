package ui.component.subcomponent.result;

import dto.detail.DTOProperty;
import dto.simulation.DTOSimulationHistogram;
import dto.simulation.DTOSimulationResult;
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
import ui.component.custom.board.BoardView;
import ui.component.custom.progress.SimulationProgressView;
import ui.engine.EngineManager;
import ui.engine.EntityInfo;
import ui.engine.Simulation;
import ui.engine.Status;
import ui.style.StyleManager;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsController {
    @FXML public ListView<Simulation> listExecution;
    @FXML public SimulationProgressView gridSeconds, gridTicks;
    @FXML public Button buttonResume, buttonPause, buttonStop, buttonRerun, buttonBoard, buttonNext;
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
            showSimulationResult(engineManager.getSimulationResult(selectedSimulation.get().getId()));
            updateDisplayData();
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

        if(simulationResult != null){
            //populateGraph(simulationResult.getId());
        }
    }

    @FXML
    public void initialize() {
        selectedSimulation.bind(listExecution.getSelectionModel().selectedItemProperty());
        selectedStatus.addListener(simulationStatusListener);
        buttonPause.setOnAction(this::actionSimulationPause);
        buttonResume.setOnAction(this::actionSimulationResume);
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

        buttonPause.disableProperty().bind(selectedStatus.isEqualTo(Status.RUNNING).not());
        buttonResume.disableProperty().bind(selectedStatus.isEqualTo(Status.PAUSED).not());
        buttonStop.disableProperty().bind(Bindings.and(
                selectedStatus.isNotEqualTo(Status.RUNNING),
                selectedStatus.isNotEqualTo(Status.PAUSED)));
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
        engineManager.pauseSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationStop(ActionEvent actionEvent) {
        engineManager.stopSimulation(selectedSimulation.get().getId());
    }

    private void actionSimulationResume(ActionEvent actionEvent) {
        engineManager.resumeSimulation(selectedSimulation.get().getId());
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

    /*
    private void populateGraph(int id){
        chartPopulationPerTick.getData().clear();
        for (EntityInfo entity : selectedSimulation.get().getEntities()){
            chartPopulationPerTick.getData().add(selectedSimulation.get().getEntityPopulationSeriesMap().get(entity));
        }
    }*/

    /*private void populateEntityTable(int simulationId) {
        // Clear existing items in the table and reset the series
        entityTable.getItems().clear();

        // Get entity information from the engineManager
        Collection<DTOEntityPopulation> entityPopulations = engineManager.getDetailsByEntityCount(simulationId);

        // Populate the table with entity information
        for (DTOEntityPopulation entityPopulation : entityPopulations) {
            String entityName = entityPopulation.getEntity().getName();

            *//*
            Map<String, XYChart.Series> stringSeriesMap = seriesMap.computeIfAbsent(simulationId, k -> new HashMap<>());
            XYChart.Series series = stringSeriesMap.computeIfAbsent(entityName, k -> new XYChart.Series());
            series.setName(entityName); // Set the name for the series

            double ticks = engineManager.getSimulations().get(simulationId).getProgressTicks().getValue();

             *//*
            int population = entityPopulation.getFinalPopulation();
            entityTable.getItems().add(new EntityInfo(entityName, population));

            *//*
            if (ticks % 10 == 0) {
                // Add data points to the LineChart series
                series.getData().add(new XYChart.Data<>(ticks, population));
                stringSeriesMap.put(entityName, series);
                seriesMap.put(simulationId, stringSeriesMap);
            }

             *//*
        }
    }*/
}
