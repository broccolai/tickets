package love.broccolai.tickets.minecraft.common.listener;

import com.google.inject.Inject;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.AssociatedAction;
import love.broccolai.tickets.api.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ActionListener implements PGNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(ActionListener.class);

    private final StorageService storageService;

    @Inject
    public ActionListener(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void notification(int processId, String channelName, String payload) {
        logger.info("Received notification from: {} - {}", channelName, payload);

        AssociatedAction associatedAction = this.storageService.selectActionWithTicketReference(Integer.parseInt(payload));
        Ticket ticket = this.storageService.selectTicket(associatedAction.ticket()).orElseThrow();
        Action action = associatedAction.action();

        logger.info("ticket {} has received action {}", ticket.id(), action.getClass().getSimpleName());
    }
}
