package ui.engine;

import dto.detail.DTOTermination;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Simulation {

    private final int id;
    private final LocalDateTime runDate;

    private final Progress progressSeconds;
    private final Progress progressTicks;
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.RUNNING);
    private final ObservableList<EntityInfo> entities = FXCollections.observableArrayList();
    private final Map<EntityInfo, XYChart.Series<Integer, Integer>> entityPopulationSeriesMap = new LinkedHashMap<>();
    private final ObservableList<XYChart.Series<Integer, Integer>> entityPopulationSeriesList = FXCollections.observableArrayList();

    public Simulation(int id, LocalDateTime runDate, DTOTermination termination) {
        this.id = id;
        this.runDate = runDate;
        progressSeconds = new Progress(termination.getCondition("SECONDS")!=null ? ((Long) termination.getCondition("SECONDS").getCondition()).intValue() : null);
        progressTicks = new Progress(termination.getCondition("TICKS")!=null ? ((Long) termination.getCondition("TICKS").getCondition()).intValue() : null);
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getRunDate() {
        return runDate;
    }

    public Progress getProgressSeconds() {
        return progressSeconds;
    }

    public Progress getProgressTicks() {
        return progressTicks;
    }

    public Status getStatus() {
        return status.get();
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public ObservableList<EntityInfo> getEntities() {
        return entities;
    }

    public void logPopulation(EntityInfo entity){
        if(!entityPopulationSeriesMap.containsKey(entity)){
            XYChart.Series<Integer, Integer> series =  new XYChart.Series<>();
            entityPopulationSeriesMap.put(entity,  series);
            entityPopulationSeriesList.add(series);
        }

        XYChart.Data<Integer, Integer> data = new XYChart.Data<>(
                (int) getProgressTicks().getValue(),
                entity.getInstanceCount());
        entityPopulationSeriesMap.get(entity).getData().add(data);
    }

    public Map<EntityInfo, XYChart.Series<Integer, Integer>> getEntityPopulationSeriesMap() {
        return entityPopulationSeriesMap;
    }

    public ObservableList<XYChart.Series<Integer, Integer>> getEntityPopulationSeriesList() {
        return entityPopulationSeriesList;
    }
}
