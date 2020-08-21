package broccolai.tickets.user;

public class UserSettings {
    Boolean announcements;

    public UserSettings(Boolean announcements) {
        this.announcements = announcements;
    }

    public Boolean getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Boolean announcements) {
        this.announcements = announcements;
    }
}