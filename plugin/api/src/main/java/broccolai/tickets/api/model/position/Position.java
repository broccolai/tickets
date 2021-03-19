package broccolai.tickets.api.model.position;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class Position {

    private final @Nullable String world;
    private final int x;
    private final int y;
    private final int z;

    public Position(final @Nullable String world, final int x, final int y, final int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public @Nullable String world() {
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
