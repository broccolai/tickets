package broccolai.tickets.core.service.message;

import broccolai.tickets.core.configuration.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.message.IMessageSource;

@Singleton
public final class MessageSource implements IMessageSource<Audience, String> {

    private final LocaleConfiguration localeConfiguration;

    @Inject
    public MessageSource(final LocaleConfiguration localeConfiguration) {
        this.localeConfiguration = localeConfiguration;
    }

    @Override
    public String messageOf(final Audience receiver, final String messageKey) {
        return this.localeConfiguration.get(messageKey);
    }

}
