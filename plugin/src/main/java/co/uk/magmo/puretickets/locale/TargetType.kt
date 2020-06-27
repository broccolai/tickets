package co.uk.magmo.puretickets.locale


enum class TargetType(val hasPrefix: Boolean) {
    SENDER(true), NOTIFICATION(true), ANNOUNCEMENT(true), DISCORD(false);
}