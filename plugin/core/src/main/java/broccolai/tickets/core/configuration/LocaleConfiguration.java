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

    public SenderLocaleConfiguration sender = new SenderLocaleConfiguration();

    public NotifyLocaleConfiguration notify = new NotifyLocaleConfiguration();

    public AnnouncementLocaleConfiguration announcement = new AnnouncementLocaleConfiguration();

    public TitleLocaleConfiguration title = new TitleLocaleConfiguration();

    public ShowLocaleConfiguration show = new ShowLocaleConfiguration();

    public ExceptionLocaleConfiguration exception = new ExceptionLocaleConfiguration();

    public FormatLocaleConfiguration format = new FormatLocaleConfiguration();

    @ConfigSerializable
    public static final class SenderLocaleConfiguration {

        public LocaleEntry create = new LocaleEntry("<prefix> Ticket <ticket> created");

        public LocaleEntry update = new LocaleEntry("<prefix> Ticket <ticket> updated");

        public LocaleEntry close = new LocaleEntry("<prefix> Ticket <ticket> closed");

        public LocaleEntry claim = new LocaleEntry("<prefix> Ticket <ticket> claimed");

        public LocaleEntry unclaim = new LocaleEntry("<prefix> Ticket <ticket> unclaimed");

        public LocaleEntry assign = new LocaleEntry("<prefix> Ticket <ticket> assigned to <target>");

        public LocaleEntry complete = new LocaleEntry("<prefix> Ticket <ticket> completed");

        public LocaleEntry reopen = new LocaleEntry("<prefix> Ticket <ticket> completed");

        public LocaleEntry note = new LocaleEntry("<prefix> Note added to ticket <ticket>");

        public LocaleEntry teleport = new LocaleEntry("<prefix> Teleported to ticket <ticket> creation location");

    }

    @ConfigSerializable
    public static final class NotifyLocaleConfiguration {

        public LocaleEntry claim = new LocaleEntry("<prefix> <player> has claimed your ticket");

        public LocaleEntry unclaim = new LocaleEntry("<prefix> <player> has yielded your ticket");

        public LocaleEntry assign = new LocaleEntry("<prefix> <player> has assigned you to ticket <id>");

        public LocaleEntry complete = new LocaleEntry("<prefix> <player> has completed your ticket");

        public LocaleEntry reopen = new LocaleEntry("<prefix> <player> has reopened your ticket");

        public LocaleEntry note = new LocaleEntry("<prefix> <player> has added a note to your ticket <note>");

    }

    @ConfigSerializable
    public static final class AnnouncementLocaleConfiguration {

        public LocaleEntry create = new LocaleEntry("<prefix> <player> opened a new ticket <ticket><white> - <message>");

        public LocaleEntry update = new LocaleEntry("<prefix> <player> updated ticket <ticket><white> - <message>");

        public LocaleEntry close = new LocaleEntry("<prefix> <player> closed their ticket <ticket>");

        public LocaleEntry claim = new LocaleEntry("<prefix> <player> claimed ticket <ticket>");

        public LocaleEntry unclaim = new LocaleEntry("<prefix> <player> unclaimed ticket <ticket>");

        public LocaleEntry assign = new LocaleEntry("<prefix> <player> assigned ticket <ticket><white> to <target>");

        public LocaleEntry complete = new LocaleEntry("<prefix> <player> completed ticket <ticket>");

        public LocaleEntry reopen = new LocaleEntry("<prefix> <player> reopened ticket <ticket>");

        public LocaleEntry note = new LocaleEntry("<prefix> <player> added a note to ticket <ticket><white> - <note>");

    }

    @ConfigSerializable
    public static final class TitleLocaleConfiguration {

        public LocaleEntry wrapper = new LocaleEntry(" <dark_gray><bold><strikethrough>====<reset> ");

        public LocaleEntry allTickets = new LocaleEntry("<wrapper><color:#f5a5a5>Tickets<wrapper>");

        public LocaleEntry yourTickets = new LocaleEntry("<wrapper><color:#f5a5a5>Tickets<wrapper>");

        public LocaleEntry showTicket = new LocaleEntry("<wrapper><color:#f5a5a5>Ticket <ticket><wrapper>");

        public LocaleEntry highscores = new LocaleEntry("<wrapper><color:#f5a5a5>Highscores<wrapper>");

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
    public static final class ExceptionLocaleConfiguration {

        public LocaleEntry noPermission = new LocaleEntry("<prefix> You do not have permission for this command");

        public LocaleEntry invalidSender = new LocaleEntry("<prefix> This command must be executed by <sender>");

        public LocaleEntry ticketNotFound = new LocaleEntry("<prefix> Ticket could not be found");

        public LocaleEntry invalidSettingType = new LocaleEntry("<prefix> Invalid setting type");

        public LocaleEntry tooManyOpenTickets = new LocaleEntry("<prefix> You have too many open tickets, the limit is <limit>");

        public LocaleEntry ticketClosed = new LocaleEntry("<prefix> This ticket is closed");

        public LocaleEntry ticketOpen = new LocaleEntry("<prefix> This ticket is already open");

        public LocaleEntry ticketClaimed = new LocaleEntry("<prefix> This ticket is already claimed");

    }

    @ConfigSerializable
    public static final class FormatLocaleConfiguration {

        public LocaleEntry list = new LocaleEntry("<ticket> <dark_gray>- <white><message>");

        public LocaleEntry listHeader = new LocaleEntry("<green><player>");

        public LocaleEntry log = new LocaleEntry("<yellow><bold><action> - <white><player>");

        public LocaleEntry status = new LocaleEntry("<amount> <status>");

        public LocaleEntry hs = new LocaleEntry("<green><target> <dark_gray>- <white><bold><amount>");

        public LocaleEntry reminder = new LocaleEntry("<prefix> There are <amount> ticket(s) currently open");

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
