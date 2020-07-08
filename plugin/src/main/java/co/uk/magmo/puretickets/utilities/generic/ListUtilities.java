package co.uk.magmo.puretickets.utilities.generic;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtilities {
    public static <T> List<T> filter(List<T> input, Predicate<T> predicate) {
        List<T> output = new ArrayList<>();

        for (T value : input) {
            if (predicate.test(value)) {
                output.add(value);
            }
        }

        return output;
    }

    public static <T> List<T> filter(T[] input, Predicate<T> predicate) {
        return filter(Arrays.asList(input), predicate);
    }

    public static <R, T> List<R> map(List<T> input, Function<T, R> function) {
        List<R> output = new ArrayList<>();

        input.forEach(value -> output.add(function.apply(value)));

        return output;
    }

    public static <R, T> List<R> map(T[] input, Function<T, R> function) {
        return map(Arrays.asList(input), function);
    }

    public static <R, T> Map<R, List<T>> group(List<T> input, Function<T, R> function) {
        Map<R, List<T>> output = new HashMap<>();

        input.forEach(value -> {
            R type = function.apply(value);
            output.putIfAbsent(type, new ArrayList<>());

            List<T> current = output.get(type);
            current.add(value);

            output.put(type, current);
        });

        return output;
    }
}
