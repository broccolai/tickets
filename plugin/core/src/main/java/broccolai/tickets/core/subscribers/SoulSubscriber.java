package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.impl.SoulJoinEvent;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.storage.StorageService;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public final class SoulSubscriber implements Subscriber {

    private final StorageService storageService;

    @Inject
    public SoulSubscriber(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Subscribe
    public void onSoulJoin(final @NonNull SoulJoinEvent event) {
        PlayerSoul soul = event.soul();
        Collection<Component> notifications = this.storageService.notifications(soul);

        for (final Component notification : notifications) {
            soul.sendMessage(notification);
        }
    }

}
