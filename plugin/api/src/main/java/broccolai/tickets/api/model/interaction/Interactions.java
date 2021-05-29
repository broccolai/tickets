package broccolai.tickets.api.model.interaction;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface Interactions extends Iterable<Interaction> {

    void add(@NonNull Interaction interaction);

    @NonNull Collection<@NonNull Interaction> find(@Nullable Predicate<Interaction> predicate);

    @NonNull Optional<@NonNull Interaction> findLatest(@Nullable Predicate<Interaction> predicate);

    @NonNull Optional<@NonNull MessageInteraction> findLatestMessage(@Nullable Predicate<MessageInteraction> predicate);

}
