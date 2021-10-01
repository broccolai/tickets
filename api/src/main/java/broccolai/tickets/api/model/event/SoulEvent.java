package broccolai.tickets.api.model.event;

import broccolai.tickets.api.model.user.Soul;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface SoulEvent extends Event {

    @NonNull Soul soul();

}
