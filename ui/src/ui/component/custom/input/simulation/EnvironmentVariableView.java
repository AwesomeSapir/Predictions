package ui.component.custom.input.simulation;

import dto.detail.DTOEnvironmentVariable;
import ui.component.custom.input.generic.*;

public class EnvironmentVariableView  {

    private final InputItemView view;
    private DTOEnvironmentVariable environmentVariable;

    public EnvironmentVariableView(DTOEnvironmentVariable environmentVariable) {
        this.environmentVariable = environmentVariable;
        switch (environmentVariable.getType()){
            case "DECIMAL":
            case "FLOAT":
                if(environmentVariable.getRange() != null) {
                    view = new RangedNumericItemView(environmentVariable.getRange().getFrom(), environmentVariable.getRange().getTo());
                } else {
                    view = new NumericItemView();
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
