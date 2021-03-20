package broccolai.tickets.bukkit.listeners;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.api.service.user.UserService;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public final class BungeeListener implements PluginMessageListener {

    private final TicketService ticketService;
    private final UserService userService;
    private final MessageService messageService;

    @Inject
    public BungeeListener(
            final @NonNull TicketService ticketService,
            final @NonNull UserService userService,
            final @NonNull MessageService messageService
    ) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void onPluginMessageReceived(
            final @NotNull String channel,
            final @NotNull Player player,
            final byte @NotNull [] message
    ) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (!subchannel.equals("Tickets")) {
            return;
        }

        short length = in.readShort();
        byte[] messageBytes = new byte[length];
        in.readFully(messageBytes);

        DataInputStream messageIn = new DataInputStream(new ByteArrayInputStream(messageBytes));

        int id;

        try {
            id = messageIn.readInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Optional<Ticket> potentialTicket = this.ticketService.get(id);

        if (!potentialTicket.isPresent()) {
            return;
        }

        Ticket ticket = potentialTicket.get();

        Component component = this.messageService.staffTicketCreate(ticket);

        this.userService.players().forEach(soul -> {
            if (soul.permission("tickets.staff.announce")) {
                soul.sendMessage(component);
            }
        });
    }

}
