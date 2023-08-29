package ui.customcomponent.view;

import dto.detail.DTOEnvironmentVariable;
import ui.customcomponent.view.item.BooleanItemView;
import ui.customcomponent.view.item.InputItemView;
import ui.customcomponent.view.item.NumericItemView;
import ui.customcomponent.view.item.StringItemView;

public class EnvironmentVariableView  {

    private final InputItemView view;
    private DTOEnvironmentVariable environmentVariable;

    public EnvironmentVariableView(DTOEnvironmentVariable environmentVariable) {
        this.environmentVariable = environmentVariable;
        switch (environmentVariable.getType()){
            case "DECIMAL":
            case "FLOAT":
                view = new NumericItemView();
                if(environmentVariable.getRange() != null) {
                    ((NumericItemView) view).setRange(environmentVariable.getRange());
                }
                break;
            case "BOOLEAN":
                view = new BooleanItemView();
                break;
            case "STRING":
                view = new StringItemView();
                break;
            default:
                throw new IllegalArgumentException("Invalid variable type: " + environmentVariable.getType());
        }
        view.setTitle(environmentVariable.getName());
    }

    public InputItemView getView() {
        return view;
    }
}
