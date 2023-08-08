package validator;

import world.type.Range;

public class Validator {

    private boolean isValid = true;
    private final String input;

    private Validator(String input){
        this.input = input;
    }

    public static Validator validate(String input){
        return new Validator(input);
    }

    public boolean isValid(){
        return isValid;
    }

    public Validator isInteger(){
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return this;
    }

    public Validator isDouble(){
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return this;
    }

    public Validator isInRange(double from, double to){
        if (isDouble().isValid) {
            double value = Double.parseDouble(input);
            if(value > to || value < from){
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return this;
    }

    public Validator isInRange(Range range){
        if (isDouble().isValid) {
            double value = Double.parseDouble(input);
            if(value > range.to() || value < range.from()){
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return this;
    }

    public Validator isBoolean(){
        if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) {
            isValid = false;
        }
        return this;
    }

    public Validator isOnlyLetters(){
        if(!input.matches("^[a-zA-Z]+$")){
            isValid = false;
        }
        return this;
    }
}
