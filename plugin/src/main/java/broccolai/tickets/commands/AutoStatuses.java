package broccolai.tickets.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

/**
 * An annotation to represent that statuses a argument could use.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface AutoStatuses {
    /**
     * Get the string value.
     * @return the ticket statuses, separated by commas
     */
    @NotNull
    String value();
}
