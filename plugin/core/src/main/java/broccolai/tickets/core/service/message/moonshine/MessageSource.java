package broccolai.tickets.core.service.message.moonshine;

import broccolai.tickets.core.configuration.NewLocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.message.IMessageSource;

@Singleton
public final class MessageSource implements IMessageSource<Audience, String> {

    private final NewLocaleConfiguration localeConfiguration;

    @Inject
    public MessageSource(final NewLocaleConfiguration localeConfiguration) {
        this.localeConfiguration = localeConfiguration;
    }

    @Override
    public String messageOf(final Audience receiver, final String messageKey) {
        return this.localeConfiguration.get(messageKey);
    }

}
