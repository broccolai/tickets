package love.broccolai.tickets.minecraft.common.model;

public record Location(
    String world,
    double x,
    double y,
    double z,
    float yaw,
    float pitch
) {
}
