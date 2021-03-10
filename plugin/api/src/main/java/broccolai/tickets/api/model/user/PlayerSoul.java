package broccolai.tickets.api.model.user;

import broccolai.tickets.api.model.position.Position;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PlayerSoul extends OnlineSoul {

    @NonNull Position position();

    void teleport(@NonNull Position location);

}
