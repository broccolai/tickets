package broccolai.tickets.core.configuration;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public final class TasksConfiguration {

    @Comment("How often to remind staff of the current open tickets, in minutes")
    public ReminderTaskConfiguration reminder = new ReminderTaskConfiguration();

    @Comment("How long to wait after a player joins to send the reminder message, in seconds")
    public int joinReminderDelay = 5;

    @ConfigSerializable
    public static final class ReminderTaskConfiguration {

        public int delay = 2;

        public int repeat = 5;

    }

}
