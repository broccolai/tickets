package broccolai.tickets.user;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * class representing a users setting choices
 */
public final class UserSettings {

    private boolean announcements;

    /**
     * Initialise a User Setting instance with all values
     *
     * @param announcements true for wanting announcements
     */
    public UserSettings(final boolean announcements) {
        this.announcements = announcements;
    }

    /**
     * Set an option to a corresponding value
     *
     * @param option Option
     * @param value  Value to use
     */
    public void set(@NonNull final Options option, final boolean value) {
        if (option == Options.ANNOUNCEMENTS) {
            announcements = value;
        }
    }

    /**
     * Get the users announcement choice
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
