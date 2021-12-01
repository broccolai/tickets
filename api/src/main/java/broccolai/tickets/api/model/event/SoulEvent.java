package broccolai.tickets.api.model.event;

import broccolai.tickets.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface SoulEvent extends Event {

    @NonNull User soul();

}
