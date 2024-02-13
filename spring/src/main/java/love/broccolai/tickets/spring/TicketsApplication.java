package love.broccolai.tickets.spring;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketsApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketsApplication.class);

    /**
     * Launches the example application.
     *
     * @param args the args
     */
    public static void main(final @NonNull String @NonNull[] args) {
        SpringApplication.run(TicketsApplication.class, args);
    }

    private void temporary() {
    }
}
