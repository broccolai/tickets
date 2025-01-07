package love.broccolai.tickets.minecraft.common.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.leangen.geantyref.TypeToken;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.minecraft.common.mooonshine.BasicReceiverResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.LocaleConfiguration;
import love.broccolai.tickets.minecraft.common.mooonshine.MessageRenderer;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.LocationPlaceholderResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.NumberPlaceholderResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.ProfilePlaceholderResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.StringPlaceholderResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.TicketFormatPlaceholderResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.TicketPlaceholderResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.TicketStatusPlaceholderResolver;
import love.broccolai.tickets.minecraft.common.mooonshine.resolvers.UUIDPlaceholderResolver;
import love.broccolai.tickets.minecraft.common.service.MessageService;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.Moonshine;
import net.kyori.moonshine.exception.scan.UnscannableMethodException;
import net.kyori.moonshine.strategy.StandardPlaceholderResolverStrategy;
import net.kyori.moonshine.strategy.supertype.StandardSupertypeThenInterfaceSupertypeStrategy;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public class MessageModule extends AbstractModule {

    @Provides
    public MessageService messageService(
        final LocaleConfiguration localeConfiguration,
        final BasicReceiverResolver basicReceiverResolver,
        final MessageRenderer messageRenderer,
        final StringPlaceholderResolver stringPlaceholderResolver,
        final NumberPlaceholderResolver numberPlaceholderResolver,
        final ProfilePlaceholderResolver profilePlaceholderResolver,
        final UUIDPlaceholderResolver uuidPlaceholderResolver,
        final TicketPlaceholderResolver ticketPlaceholderResolver,
        final TicketFormatPlaceholderResolver ticketFormatPlaceholderResolver,
        final TicketStatusPlaceholderResolver ticketStatusPlaceholderResolver,
        final LocationPlaceholderResolver locationPlaceholderResolver
    ) throws UnscannableMethodException {
        return Moonshine.<MessageService, Audience>builder(TypeToken.get(MessageService.class))
            .receiverLocatorResolver(basicReceiverResolver, 0)
            .sourced((audience, key) -> localeConfiguration.get(key))
            .rendered(messageRenderer)
            .sent(Audience::sendMessage)
            .resolvingWithStrategy(
                new StandardPlaceholderResolverStrategy<>(new StandardSupertypeThenInterfaceSupertypeStrategy(true))
            )
            .weightedPlaceholderResolver(String.class, stringPlaceholderResolver, 1)
            .weightedPlaceholderResolver(Number.class, numberPlaceholderResolver, 1)
            .weightedPlaceholderResolver(Profile.class, profilePlaceholderResolver, 1)
            .weightedPlaceholderResolver(UUID.class, uuidPlaceholderResolver, 1)
            .weightedPlaceholderResolver(Ticket.class, ticketPlaceholderResolver, 1)
            .weightedPlaceholderResolver(TicketFormat.class, ticketFormatPlaceholderResolver, 1)
            .weightedPlaceholderResolver(TicketStatus.class, ticketStatusPlaceholderResolver, 1)
            .weightedPlaceholderResolver(Location.class, locationPlaceholderResolver, 1)
            .create(this.getClass().getClassLoader());
    }

    @Provides
    @Singleton
    public LocaleConfiguration provideLocaleConfiguration(
        final Path folder
    ) throws IOException {
        String localeFileName = "locale_en.conf";

        Path file = folder
            .resolve("locales")
            .resolve(localeFileName);

        ConfigurationLoader<?> loader = this.pathConfigurationLoader(file);

        ConfigurationNode node = loader.load();
        ConfigurationNode defaultNode = this.createDefaultNode(localeFileName);

        node = node.mergeFrom(defaultNode);

        loader.save(node);
        return node.visit(new LocaleConfiguration.Visitor());
    }

    private ConfigurationNode createDefaultNode(final String localeFileName) throws ConfigurateException {
        URL defaultNode = Objects.requireNonNull(MessageModule.class.getResource("/locales/" + localeFileName));

        return HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts.shouldCopyDefaults(true))
            .url(defaultNode)
            .build()
            .load();
    }

    private ConfigurationLoader<?> pathConfigurationLoader(final Path path) {
        return HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts.shouldCopyDefaults(true))
            .path(path)
            .build();
    }

}
