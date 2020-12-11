package broccolai.tickets.core.utilities;

import org.checkerframework.checker.nullness.qual.Nullable;

public class TicketLocation {

    private final String world;
    private final double x;
    private final double y;
    private final double z;

    /**
     * Construct ticket location
     *
     * @param world World
     * @param x     X
     * @param y     Y
     * @param z     Z
     */
    public TicketLocation(final @Nullable String world, final double x, final double y, final double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return Get world
     */
    public String getWorld() {
        return world;
    }

    /**
     * @return Get x
     */
    public double getX() {
        return x;
    }

    /**
     * @return Get y
     */
    public double getY() {
        return y;
    }

    /**
     * @return Get z
     */
    public double getZ() {
        return z;
    }

}
