package ui;

import validator.Validator;

import java.util.Collection;
import java.util.Scanner;

public class MenuHelper {

    private final Scanner scanner;

    public MenuHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getSelectionForCollection(Collection<?> collection, String prompt){
        boolean exit = collection.isEmpty();
        int selection = 0;
        while (!exit) {
            System.out.println(prompt);
            String input = scanner.nextLine();
            if (Validator.validate(input).isInteger().isInRange(1, collection.size()).isValid()) {
                selection = Integer.parseInt(input);
                exit = true;
            } else {
                System.out.println("Invalid selection.");
            }
        }
        return selection - 1;
    }

    public void printMenu(Collection<?> collection, String title, StringFunction stringFunction){
        int index = 1;
        if(title != null){
            System.out.println(title + ":");
        }
        for (Object object : collection){
            System.out.println((index) + ". " + stringFunction.getText(object));
            index++;
        }
        if(collection.isEmpty()){
            System.out.println("There are none to display.");
        }
    }

    public void printMenu(Collection<?> collection, StringFunction stringFunction){
        printMenu(collection, null, stringFunction);
    }

}
