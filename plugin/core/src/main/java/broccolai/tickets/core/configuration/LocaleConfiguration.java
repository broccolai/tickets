package broccolai.tickets.core.configuration;

import broccolai.tickets.core.model.locale.LocaleEntry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public final class LocaleConfiguration {

    public LocaleEntry prefix = new LocaleEntry("<color:#f5a5a5><bold>T <dark_gray>»<reset>");

    public TitleLocaleConfiguration title = new TitleLocaleConfiguration();

    public ShowLocaleConfiguration show = new ShowLocaleConfiguration();

    public FormatLocaleConfiguration format = new FormatLocaleConfiguration();

    @ConfigSerializable
    public static final class TitleLocaleConfiguration {

        public LocaleEntry wrapper = new LocaleEntry(" <dark_gray><bold><strikethrough>====<reset> ");

        public LocaleEntry allTickets = new LocaleEntry("<wrapper><color:#f5a5a5>Tickets<wrapper>");

        public LocaleEntry yourTickets = new LocaleEntry("<wrapper><color:#f5a5a5>Tickets<wrapper>");

        public LocaleEntry showTicket = new LocaleEntry("<wrapper><color:#f5a5a5>Ticket <ticket><wrapper>");

        public LocaleEntry highscores = new LocaleEntry("<wrapper><color:#f5a5a5>Highscores<wrapper>");

        public LocaleEntry log = new LocaleEntry("<wrapper><color:#f5a5a5>Log<wrapper>");

    }

    @ConfigSerializable
    public static final class ShowLocaleConfiguration {

        public LocaleEntry status = new LocaleEntry("<white>Status<dark_gray>: <yellow><status>");

        public LocaleEntry player = new LocaleEntry("<white>Created<dark_gray>: <yellow><player>");

        public LocaleEntry position = new LocaleEntry("<white>Position<dark_gray>: <yellow><position>");

        public LocaleEntry claimed = new LocaleEntry("<white>Claimer<dark_gray>: <yellow><claimer>");

        public LocaleEntry unclaimed = new LocaleEntry("<white>Claimer<dark_gray>: <yellow>NONE");

        public LocaleEntry message = new LocaleEntry("<white>Message<dark_gray>: <yellow><message>");

    }

    @ConfigSerializable
    public static final class FormatLocaleConfiguration {

        public LocaleEntry list = new LocaleEntry("<ticket> <dark_gray>- <white><message>");

        public LocaleEntry listHeader = new LocaleEntry("<green><player>");

        public LocaleEntry log = new LocaleEntry("<yellow><bold><action> - <white><player>");

        public LocaleEntry hs = new LocaleEntry("<green><player> <dark_gray>- <white><bold><amount>");

    }

    //region Configurate
    private static final @NonNull ObjectMapper<LocaleConfiguration> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(LocaleConfiguration.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static LocaleConfiguration loadFrom(final @NonNull ConfigurationNode node) {
        try {
            return MAPPER.load(node);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public <N extends ScopedConfigurationNode<N>> void saveTo(final @NonNull N node) {
        try {
            MAPPER.save(this, node);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    //endregion

}
