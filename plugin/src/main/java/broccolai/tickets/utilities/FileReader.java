package broccolai.tickets.utilities;

import broccolai.tickets.PureTickets;
import com.google.common.io.CharStreams;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class FileReader {

    private FileReader() {
    }

    /**
     * Load all content from a file
     *
     * @param file File path relative to plugin
     * @return Files content
     */
    public static String fromPath(final @NonNull String file) {
        try (InputStream stream = PureTickets.class.getResourceAsStream(file);
             Reader reader = new InputStreamReader(stream)) {
            return CharStreams.toString(reader);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not read file: " + file);
        }
    }

}
