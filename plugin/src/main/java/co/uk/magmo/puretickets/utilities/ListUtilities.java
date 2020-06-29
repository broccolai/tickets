package co.uk.magmo.puretickets.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListUtilities {
    public static <R, T> ArrayList<R> map(List<T> input, Function<T, R> function) {
        ArrayList<R> output = new ArrayList<>();

        input.forEach(value -> output.add(function.apply(value)));

        return output;
    }
}
