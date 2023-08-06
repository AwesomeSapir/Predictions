import validator.Validator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            System.out.println("is boolean: " + Validator
                    .validate(input)
                    .isBoolean()
                    .isValid());
            System.out.println("is name: " + Validator
                    .validate(input)
                    .isOnlyLetters()
                    .isValid());
            System.out.println("is double: " + Validator
                    .validate(input)
                    .isDouble()
                    .isValid());
            System.out.println("is integer: " + Validator
                    .validate(input)
                    .isInteger()
                    .isValid());
        }
    }
}