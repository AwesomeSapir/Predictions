package ui.component.custom.input.simulation;

import dto.detail.DTOEnvironmentVariable;
import ui.component.custom.input.generic.*;

public class EnvironmentVariableView {

    private EnvironmentVariableView(){}

    public static InputItemView<?> create(DTOEnvironmentVariable environmentVariable){
        InputItemView<?> view;
        switch (environmentVariable.getType()){
            case "DECIMAL":
            case "FLOAT":
                if(environmentVariable.getRange() != null) {
                    view = new RangedNumericItemView(environmentVariable.getRange().getFrom(), environmentVariable.getRange().getTo(), false);
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
        return view;
    }
}
