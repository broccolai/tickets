package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import co.uk.magmo.puretickets.interactions.PendingNotification;
import com.google.common.collect.ArrayListMultimap;

import java.util.UUID;

public interface NotificationFunctions {
    ArrayListMultimap<UUID, PendingNotification> selectAllAndClear();

    void insertAll(ArrayListMultimap<UUID, PendingNotification> notifications);
}

