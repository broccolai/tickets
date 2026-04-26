package love.broccolai.tickets.api.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.action.TicketActionListener;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.profile.Profile;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface StorageService {

    void addTicketActionRelay(TicketActionListener listener);

    Ticket createTicket(UUID creator, TicketFormat type, TicketFormData form);

    void appendAction(Ticket ticket, TicketAction action);

    Optional<Ticket> selectTicket(int ticketId);

    Map<Integer, Ticket> selectTickets(int... ticketIds);

    Collection<Ticket> findTickets(TicketSearch search);

    Collection<Profile> loadProfiles(Collection<UUID> uniqueIds);

    Optional<Profile> findProfile(String name);

    void insertProfile(Profile profile);

    void updateProfile(Profile profile);
}
