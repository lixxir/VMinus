package net.lixir.vminus;

public class NumberUtil {
    public static double modifyNumber(double existingNumber, double number, String operation) {
        switch (operation.toLowerCase()) {
            case "addition":
                return existingNumber + number;
            case "subtraction":
                return existingNumber - number;
            case "exponent":
                return Math.pow(existingNumber, number);
            case "divide":
                return existingNumber / number;
            case "multiply":
                return existingNumber * number;
            default:
                return number;
        }
    }
}
