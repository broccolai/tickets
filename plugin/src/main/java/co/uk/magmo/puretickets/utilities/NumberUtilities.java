package co.uk.magmo.puretickets.utilities;

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
