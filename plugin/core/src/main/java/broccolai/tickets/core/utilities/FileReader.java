package broccolai.tickets.core.utilities;

import broccolai.tickets.core.PureTickets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class FileReader {

    private FileReader() {
    }

    public static @NonNull String fromPath(final @NonNull String file) {
        try (InputStream stream = PureTickets.class.getResourceAsStream(file);
             Reader reader = new InputStreamReader(stream)) {
            return CharStreams.toString(reader);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not read file: " + file);
        }
    }

}
