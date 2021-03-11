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

    }

    @ConfigSerializable
    public static final class TicketsConfiguration {

        public AliasConfiguration claim = new AliasConfiguration("claim", "c");

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
