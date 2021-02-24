package broccolai.tickets.api.model.position;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class Position {

    private final @NonNull String world;
    private final int x;
    private final int y;
    private final int z;

    public Position(final @NonNull String world, final int x, final int y, final int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public @NonNull String world() {
        return this.world;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int z() {
        return this.z;
    }

}
