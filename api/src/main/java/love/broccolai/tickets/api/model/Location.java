package love.broccolai.tickets.api.model;

public record Location(
    String world,
    double x,
    double y,
    double z,
    float yaw,
    float pitch
) {
}
