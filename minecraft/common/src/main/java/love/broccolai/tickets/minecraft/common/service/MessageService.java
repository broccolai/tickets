package love.broccolai.tickets.minecraft.common.service;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.minecraft.common.TicketPermissions;
import love.broccolai.tickets.minecraft.common.moonshine.annotations.Causer;
import love.broccolai.tickets.minecraft.common.moonshine.annotations.PermissionReceiver;
import love.broccolai.tickets.minecraft.common.moonshine.annotations.Receiver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.annotation.Message;
import net.kyori.moonshine.annotation.Placeholder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MessageService {

    @Message("ticket.header")
    Component ticketHeader(@Placeholder Ticket ticket);

    @Message("ticket.subheader")
    Component ticketSubheader(@Placeholder Ticket ticket);

    @Message("ticket.status.unclaimed")
    Component ticketStatusUnclaimed(@Placeholder Ticket ticket);

    @Message("ticket.status.claimed")
    Component ticketStatusClaimed(@Placeholder Ticket ticket);

    @Message("ticket.data")
    Component ticketData(@Placeholder TicketDataField field);

    @Message("ticket.timeline.header")
    Component ticketTimelineHeader();

    @Message("ticket.timeline.entry")
    Component ticketTimelineEntry(
        @Placeholder String title,
        @Placeholder Instant date,
        @Placeholder UUID creator
    );

    @Message("feedback.user.create")
    void feedbackUserCreate(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.user.list_header")
    Component feedbackUserListHeader();

    @Message("feedback.user.list_empty")
    Component feedbackUserListEmpty();

    @Message("feedback.user.list_entry")
    Component feedbackUserListEntry(@Placeholder Ticket ticket);

    @Message("feedback.user.comment")
    void feedbackUserComment(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.user.close")
    void feedbackUserClose(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.list_header")
    Component feedbackStaffListHeader();

    @Message("feedback.staff.list_empty")
    Component feedbackStaffListEmpty();

    @Message("feedback.staff.list_group")
    Component feedbackStaffListGroup(@Placeholder String name);

    @Message("feedback.staff.list_entry")
    Component feedbackStaffListEntry(@Placeholder Ticket ticket);

    @Message("feedback.staff.claim")
    void feedbackStaffClaim(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.assign")
    void feedbackStaffAssign(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.unclaim")
    void feedbackStaffUnclaim(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.close")
    void feedbackStaffClose(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.reopen")
    void feedbackStaffReopen(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.note")
    void feedbackStaffNote(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.teleport")
    void feedbackStaffTeleport(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.staff.teleport_missing_location")
    void feedbackStaffTeleportMissingLocation(@Receiver Audience audience, @Placeholder Ticket ticket);

    @Message("feedback.admin.average_lifespan")
    void feedbackAdminAverageLifespan(@Receiver Audience audience, @Placeholder String duration);

    @Message("feedback.error.invalid_ticket")
    void feedbackErrorInvalidTicket(@Receiver Audience audience);

    @Message("feedback.error.ticket_not_found")
    void feedbackErrorTicketNotFound(@Receiver Audience audience);

    @Message("feedback.error.invalid_profile")
    void feedbackErrorInvalidProfile(@Receiver Audience audience);

    @Message("feedback.error.profile_not_found")
    void feedbackErrorProfileNotFound(@Receiver Audience audience);

    @Message("feedback.error.ticket_type_not_found")
    void feedbackErrorTicketTypeNotFound(@Receiver Audience audience);

    @PermissionReceiver(permission = TicketPermissions.STAFF_NOTIFY)
    @Message("notification.staff.created")
    void notificationStaffCreated(@Causer UUID creator, @Placeholder Ticket ticket);

    @PermissionReceiver(permission = TicketPermissions.STAFF_NOTIFY)
    @Message("notification.staff.commented")
    void notificationStaffCommented(@Causer UUID creator, @Placeholder Ticket ticket);

    @PermissionReceiver(permission = TicketPermissions.STAFF_NOTIFY)
    @Message("notification.staff.claimed")
    void notificationStaffClaimed(@Causer UUID creator, @Placeholder Ticket ticket);

    @PermissionReceiver(permission = TicketPermissions.STAFF_NOTIFY)
    @Message("notification.staff.assigned")
    void notificationStaffAssigned(@Causer UUID creator, @Placeholder Ticket ticket);

    @PermissionReceiver(permission = TicketPermissions.STAFF_NOTIFY)
    @Message("notification.staff.unclaimed")
    void notificationStaffUnclaimed(@Causer UUID creator, @Placeholder Ticket ticket);

    @PermissionReceiver(permission = TicketPermissions.STAFF_NOTIFY)
    @Message("notification.staff.closed")
    void notificationStaffClosed(@Causer UUID creator, @Placeholder Ticket ticket);

    @PermissionReceiver(permission = TicketPermissions.STAFF_NOTIFY)
    @Message("notification.staff.reopened")
    void notificationStaffReopened(@Causer UUID creator, @Placeholder Ticket ticket);
}
