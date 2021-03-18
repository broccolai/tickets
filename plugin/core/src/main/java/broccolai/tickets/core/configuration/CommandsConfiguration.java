package broccolai.tickets.core.configuration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class CommandsConfiguration {

    public TicketConfiguration ticket = new TicketConfiguration();

    public TicketsConfiguration tickets = new TicketsConfiguration();

    @ConfigSerializable
    public static final class TicketConfiguration {

        public AliasConfiguration create = new AliasConfiguration("create", "c");

        public AliasConfiguration update = new AliasConfiguration("update", "u");

        public AliasConfiguration close = new AliasConfiguration("close", "cl");

        public AliasConfiguration list = new AliasConfiguration("list", "l");

    }

    @ConfigSerializable
    public static final class TicketsConfiguration {

        public AliasConfiguration show = new AliasConfiguration("show", "s");

        public AliasConfiguration claim = new AliasConfiguration("claim", "c");

        public AliasConfiguration complete = new AliasConfiguration("done", "d");

        public AliasConfiguration assign = new AliasConfiguration("assign", "a");

        public AliasConfiguration unclaim = new AliasConfiguration("unclaim", "u");

        public AliasConfiguration reopen = new AliasConfiguration("reopen", "r");

        public AliasConfiguration teleport = new AliasConfiguration("teleport", "tp");

        public AliasConfiguration note = new AliasConfiguration("note", "n");

        public AliasConfiguration list = new AliasConfiguration("list", "l");

    }

    @ConfigSerializable
    public static final class AliasConfiguration {

        public AliasConfiguration() {

        }

        public AliasConfiguration(final @NonNull String main, final @NonNull String... aliases) {
            this.main = main;
            this.aliases = aliases;
        }

        @Setting
        public String main = "";

        @Setting
        public String[] aliases = new String[0];

    }
}
