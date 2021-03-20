package broccolai.tickets.bukkit.subscriptions;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.impl.TicketCreateEvent;
import broccolai.tickets.api.service.event.EventService;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public final class TicketSubscriber implements Subscriber {

    private final Plugin plugin;

    @Inject
    public TicketSubscriber(final @NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register(@NonNull final EventService eventService) {

    }

    private void onTicketCreate(final @NonNull TicketCreateEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Forward");
        out.writeUTF("ONLINE");
        out.writeUTF("Tickets");

        ByteArrayOutputStream messageBytes = new ByteArrayOutputStream();
        DataOutputStream messageOut = new DataOutputStream(messageBytes);

        try {
            messageOut.writeInt(event.ticket().id());
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.writeShort(messageBytes.toByteArray().length);
        out.write(messageBytes.toByteArray());

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        if (player == null) {
            // theoretically shouldn't happen
            return;
        }

        player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
    }

}
