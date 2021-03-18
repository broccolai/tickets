package broccolai.tickets.core.configuration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public final class MainConfiguration {

    public StorageConfiguration storageConfiguration = new StorageConfiguration();

    public DiscordConfiguration discordConfiguration = new DiscordConfiguration();

    public CommandsConfiguration commandsConfiguration = new CommandsConfiguration();

    public AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration();

    //region Configurate
    private static final @NonNull ObjectMapper<MainConfiguration> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(MainConfiguration.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MainConfiguration loadFrom(final @NonNull ConfigurationNode node) {
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
