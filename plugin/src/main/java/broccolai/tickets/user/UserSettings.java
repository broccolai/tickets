package broccolai.tickets.user;

/**
 * class representing a users setting choices.
 */
public class UserSettings {
    private boolean announcements;

    /**
     * Initialise a User Setting instance with all values.
     * @param announcements true for wanting announcements
     */
    public UserSettings(boolean announcements) {
        this.announcements = announcements;
    }

    /**
     * Get the users announcement choice.
     * @return boolean representation of choice
     */
    public boolean getAnnouncements() {
        return announcements;
    }

    /**
     * Set the users announcement choice to a new value.
     * @param announcements the boolean value
     */
    public void setAnnouncements(boolean announcements) {
        this.announcements = announcements;
    }
}