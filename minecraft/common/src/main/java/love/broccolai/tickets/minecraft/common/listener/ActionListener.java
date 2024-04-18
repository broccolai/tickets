package love.broccolai.tickets.minecraft.common.listener;

import com.impossibl.postgres.api.jdbc.PGNotificationListener;

public final class ActionListener implements PGNotificationListener {

    @Override
    public void notification(int processId, String channelName, String payload) {
        System.out.println(payload);
    }
}
