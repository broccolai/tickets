package broccolai.tickets.core.model.interaction;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.Interactions;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public final class TreeSetInteractions implements Interactions {

    private final TreeSet<Interaction> entries = new TreeSet<>(Comparator.comparing(Interaction::time));

    @Override
    public void add(final @NonNull Interaction interaction) {
        this.entries.add(interaction);
    }

    @Override
    public @NonNull Collection<@NonNull Interaction> find(
            final @Nullable Predicate<Interaction> predicate
    ) {
        Collection<Interaction> output = new HashSet<>();

        for (final Interaction interaction : this.entries) {
            if (predicate == null || predicate.test(interaction)) {
                output.add(interaction);
            }
        }

        return output;
    }

    @Override
    public @NotNull Optional<@NonNull Interaction> findLatest(
            final @Nullable Predicate<Interaction> predicate
    ) {
        Iterator<Interaction> reverseIterator = this.entries.descendingIterator();

        while (reverseIterator.hasNext()) {
            Interaction next = reverseIterator.next();

            if (predicate == null || predicate.test(next)) {
                return Optional.of(next);
            }
        }

        return Optional.empty();
    }

    @Override
    public @NonNull Optional<@NonNull MessageInteraction> findLatestMessage(
            final @Nullable Predicate<MessageInteraction> predicate
    ) {
        Iterator<Interaction> reverseIterator = this.entries.descendingIterator();

        while (reverseIterator.hasNext()) {
            Interaction next = reverseIterator.next();

            if (!(next instanceof MessageInteraction nextMessage)) {
                continue;
            }

            if (predicate == null || predicate.test(nextMessage)) {
                return Optional.of(nextMessage);
            }
        }

        return Optional.empty();
    }

    @NotNull
    @Override
    public Iterator<Interaction> iterator() {
        return this.entries.iterator();
    }

}
