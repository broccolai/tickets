package love.broccolai.tickets.minecraft.common;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketPermissions {

    public static final String ADMIN_STATS = "tickets.admin.stats";
    public static final String STAFF_NOTIFY = "tickets.staff.notify";
    public static final String STAFF_SHOW = "tickets.staff.show";
    public static final String STAFF_LIST = "tickets.staff.list";
    public static final String STAFF_CLAIM = "tickets.staff.claim";
    public static final String STAFF_ASSIGN = "tickets.staff.assign";
    public static final String STAFF_UNCLAIM = "tickets.staff.unclaim";
    public static final String STAFF_CLOSE = "tickets.staff.close";
    public static final String STAFF_REOPEN = "tickets.staff.reopen";
    public static final String STAFF_NOTE = "tickets.staff.note";
    public static final String STAFF_TELEPORT = "tickets.staff.teleport";
    public static final String USER_CREATE = "tickets.user.create";
    public static final String USER_SHOW = "tickets.user.show";
    public static final String USER_LIST = "tickets.user.list";
    public static final String USER_COMMENT = "tickets.user.comment";
    public static final String USER_CLOSE = "tickets.user.close";

    private TicketPermissions() {
    }
}
