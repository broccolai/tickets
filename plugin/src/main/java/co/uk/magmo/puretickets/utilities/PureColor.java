package co.uk.magmo.puretickets.utilities;

import org.bukkit.ChatColor;

public enum PureColor {
    GREEN(ChatColor.GREEN, "00E676"), YELLOW(ChatColor.YELLOW, "FFEA00"), RED(ChatColor.RED, "ff1744");

    private final ChatColor color;
    private final String hex;

    PureColor(ChatColor color, String hex) {
        this.color = color;
        this.hex = hex;
    }
}
