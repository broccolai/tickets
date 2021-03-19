package broccolai.tickets.core.configuration;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public final class TasksConfiguration {

    @Comment("How often to remind staff of the current open tickets, in minutes")
    public ReminderTaskConfiguration reminder = new ReminderTaskConfiguration();

    @ConfigSerializable
    public static final class ReminderTaskConfiguration {

        public int delay = 2;

        public int repeat = 5;

    }

}
