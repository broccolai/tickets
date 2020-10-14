package broccolai.tickets.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tickets.commands.Single;
import broccolai.tickets.exceptions.PureException;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import broccolai.tickets.user.Soul;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.compound.CompoundArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument;
import cloud.commandframework.types.tuples.DynamicTuple;
import cloud.commandframework.types.tuples.Pair;
import cloud.commandframework.types.tuples.Tuple;
import io.leangen.geantyref.TypeToken;
import java.util.ArrayList;
import java.util.function.BiFunction;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Ticket Argument-Mapper.
 */
public class PotentialTicketMapper extends CompoundArgument<DynamicTuple, Soul, Ticket> {

    protected PotentialTicketMapper(final boolean required,
                                    @NotNull final String name,
                                    @NotNull final Tuple names,
                                    @NotNull final Tuple types,
                                    @NotNull final Tuple parserPair,
                                    /* todo: @NotNull final Tuple suggestionPair, */
                                    @NotNull final BiFunction<Soul, DynamicTuple, Ticket> mapper) {
        super(required, name, names, parserPair, /* todo: suggestionPair, */ types, mapper, DynamicTuple::of, TypeToken.get(Ticket.class));
    }

    /**
     * Create a Ticket Mapper.
     *
     * @param parser   Parser Mode to use
     * @param statuses TicketStatuses to check against
     * @return a Ticket Mapper
     */
    public static PotentialTicketMapper create(@NotNull Parser parser, @NotNull TicketStatus... statuses) {
        if (parser == Parser.TARGET) {
            CommandArgument<Soul, OfflinePlayer> playerArgument = OfflinePlayerArgument
                .<Soul>newBuilder("target")
                .withSuggestionsProvider((context, input) -> new ArrayList<>() /* todo */)
                .build();

            CommandArgument<Soul, Integer> integerArgument = IntegerArgument
                .<Soul>newBuilder("id")
                .asOptional()
                .withSuggestionsProvider((context, input) -> {
                    OfflinePlayer target = context.get("target");

                    return Lists.map(TicketSQL.selectIds(target.getUniqueId(), statuses), Object::toString);
                })
                .build();

            return new PotentialTicketMapper(true, "ticket",
                Pair.of(playerArgument.getName(), integerArgument.getName()),
                Pair.of(OfflinePlayer.class, Integer.class),
                Pair.of(playerArgument.getParser(), integerArgument.getParser()),
                /* todo: Pair.of(playerArgument.getSuggestionsProvider(), integerArgument.getSuggestionsProvider()), */
                (sender, tuple) -> {
                    Object[] objects = tuple.toArray();
                    OfflinePlayer player = (OfflinePlayer) objects[0];
                    Integer id = (Integer) objects[1];

                    Ticket ticket;
                    if (id != null) {
                        ticket = TicketSQL.select(id);
                    } else {
                        ticket = TicketSQL.selectLastTicket(player.getUniqueId(), statuses);
                    }

                    if (ticket == null) {
                        throw new PureException(Messages.EXCEPTIONS__TICKET_NOT_FOUND);
                    }

                    return ticket;
                });
        }

        if (parser == Parser.SENDER) {
            CommandArgument<Soul, Integer> integerArgument = IntegerArgument
                .<Soul>newBuilder("id")
                .asOptional()
                .withSuggestionsProvider((context, input) -> {
                    OfflinePlayer target = context.get("target");

                    return Lists.map(TicketSQL.selectIds(target.getUniqueId(), statuses), Object::toString);
                })
                .build();

            return new PotentialTicketMapper(true, "ticket",
                Single.of(integerArgument.getName()),
                Single.of(Integer.class),
                Single.of(integerArgument.getParser()),
                /* todo: Single.of(integerArgument.getSuggestionsProvider()), */
                (sender, tuple) -> {
                    Object[] objects = tuple.toArray();
                    Integer id = (Integer) objects[0];

                    Ticket ticket;
                    if (id != null) {
                        ticket = TicketSQL.select(id);
                    } else {
                        ticket = TicketSQL.selectLastTicket(sender.getUniqueId(), statuses);
                    }

                    if (ticket == null || !ticket.getPlayerUUID().equals(sender.getUniqueId())) {
                        throw new PureException(Messages.EXCEPTIONS__TICKET_NOT_FOUND);
                    }

                    return ticket;
                });
        }

        throw new UnsupportedOperationException();
    }

}
