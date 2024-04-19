package love.broccolai.tickets.api.model.action;

public record AssociatedAction(
    int ticket,
    Action action
) {
}
