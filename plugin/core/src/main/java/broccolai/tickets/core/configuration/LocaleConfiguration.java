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

        public LocaleEntry claim = new LocaleEntry("<prefix> <user> has claimed your ticket");

        public LocaleEntry unclaim = new LocaleEntry("<prefix> <user> has yielded your ticket");

        public LocaleEntry assign = new LocaleEntry("<prefix> <user> has assigned you to ticket <id>");

        public LocaleEntry complete = new LocaleEntry("<prefix> <user> has completed your ticket");

        public LocaleEntry reopen = new LocaleEntry("<prefix> <user> has reopened your ticket");

        public LocaleEntry note = new LocaleEntry("<prefix> <user> has added a note to your ticket <note>");

    }

    @ConfigSerializable
    public static final class AnnouncementLocaleConfiguration {

        public LocaleEntry create = new LocaleEntry("<prefix> <user> opened a new ticket <ticket><white> - <message>");

        public LocaleEntry update = new LocaleEntry("<prefix> <user> updated ticket <ticket><white> - <message>");

        public LocaleEntry close = new LocaleEntry("<prefix> <user> closed their ticket <ticket>");

        public LocaleEntry claim = new LocaleEntry("<prefix> <user> claimed ticket <ticket>");

        public LocaleEntry unclaim = new LocaleEntry("<prefix> <user> unclaimed ticket <ticket>");

        public LocaleEntry assign = new LocaleEntry("<prefix> <user> assigned ticket <ticket><white> to <target>");

        public LocaleEntry complete = new LocaleEntry("<prefix> <user> completed ticket <ticket>");

        public LocaleEntry reopen = new LocaleEntry("<prefix> <user> reopened ticket <ticket>");

        public LocaleEntry note = new LocaleEntry("<prefix> <user> added a note to ticket <ticket><white> - <note>");

    }

    @ConfigSerializable
    public static final class TitleLocaleConfiguration {

        public LocaleEntry wrapper = new LocaleEntry(" <dark_gray><bold><strikethrough>====<reset> ");

        public LocaleEntry specificTickets = new LocaleEntry("<wrapper><aqua>ALL TICKETS <white>FOR <player><wrapper>");

        public LocaleEntry allTickets = new LocaleEntry("<wrapper><aqua>ALL TICKETS<wrapper>");

        public LocaleEntry yourTickets = new LocaleEntry("<wrapper><aqua>YOUR TICKETS<wrapper>");

        public LocaleEntry specificStatus = new LocaleEntry("<wrapper><aqua>TICKET TICKETS <white>FOR <player><wrapper>");

        public LocaleEntry ticketStatus = new LocaleEntry("<wrapper><aqua>TICKET STATUS<wrapper>");

        public LocaleEntry showTicket = new LocaleEntry("<wrapper><color:f5a5a5>Ticket <ticket><wrapper>");

        public LocaleEntry ticketLog = new LocaleEntry("<wrapper><aqua>TICKET LOG <ticket><wrapper>");

        public LocaleEntry highscore = new LocaleEntry("<wrapper><aqua>HIGH SCORES<wrapper>");

    }

    @ConfigSerializable
    public static final class ShowLocaleConfiguration {

        public LocaleEntry status = new LocaleEntry("<white>Status<dark_gray>: <white><status>");

        public LocaleEntry player = new LocaleEntry("<white>Created<dark_gray>: <white><player>");

        public LocaleEntry position = new LocaleEntry("<white>Position<dark_gray>: <white><position>");

        public LocaleEntry claimed = new LocaleEntry("<white>Claimer<dark_gray>: <white><claimer>");

        public LocaleEntry unclaimed = new LocaleEntry("<white>Claimer<dark_gray>: <white>NONE");

        public LocaleEntry message = new LocaleEntry("<white>Message<dark_gray>: <white><message>");

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

        public LocaleEntry log = new LocaleEntry("<white><bold><reason> <dark_gray>@ <white><date><dark_gray>");

        public LocaleEntry status = new LocaleEntry("<amount> <status>");

        public LocaleEntry hs = new LocaleEntry("<green><target> <dark_gray>- <white><bold><amount>");

        public LocaleEntry settingUpdate = new LocaleEntry("<white>Setting <green><setting> <white>is now <status>");

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
