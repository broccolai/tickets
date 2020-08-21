package broccolai.tickets.utilities.generic;

public class NumberUtilities {
    public static Integer valueOfOrNull(String input) {
        if (input == null) {
            return null;
        }

        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
