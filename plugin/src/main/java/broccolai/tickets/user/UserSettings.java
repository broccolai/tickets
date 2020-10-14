package broccolai.tickets.user;

/**
 * class representing a users setting choices.
 */
public class UserSettings {
    private boolean announcements;

    /**
     * Initialise a User Setting instance with all values.
     *
     * @param announcements true for wanting announcements
     */
    public UserSettings(boolean announcements) {
        this.announcements = announcements;
    }

    /**
     * Set an option to a corresponding value.
     *
     * @param option Option
     * @param value  Value to use
     */
    public void set(Options option, boolean value) {
        if (option == Options.ANNOUNCEMENTS) {
            announcements = value;
        }
    }

    /**
     * Get the users announcement choice.
     *
     * @return boolean representation of choice
     */
    public boolean getAnnouncements() {
        return announcements;
    }

    public enum Options {
        ANNOUNCEMENTS
    }
}