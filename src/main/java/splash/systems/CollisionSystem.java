package splash.systems;

import splash.entities.Bot;
import splash.entities.Fish;
import splash.entities.Player;
import splash.entities.World;
import java.util.Set;

public class CollisionSystem {
    private final Player player;
    private final World world;
    private final Set<String> playerCollisionLayers;

    public CollisionSystem(Player player, World world) {
        this.player = player;
        this.world = world;
        this.playerCollisionLayers = Set.of("bot");
    }

    public void checkCollisions() {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerRadius = player.getRadius();

        for (String layer : playerCollisionLayers) {
            world.getEntities().stream()
                .filter(e -> layer.equals(e.getTag()))
                .filter(e -> {
                    double dx = playerX - e.getX();
                    double dy = playerY - e.getY();
                    double distanceSquared = dx * dx + dy * dy;
                    double combinedRadius = playerRadius + e.getRadius();
                    return distanceSquared <= combinedRadius * combinedRadius;
                })
                .forEach(this::handleCollision);
        }
    }

    private void handleCollision(Fish entity) {
        if (entity instanceof Bot bot) {
            if (bot.getSize() > player.getSize() && !player.isInvulnerable()) {
                player.healthProperty().set(player.healthProperty().get() - bot.getValue());
                player.startDamageAnimation();
            } else if (bot.getSize() <= player.getSize()) {
                player.pointsProperty().set(player.pointsProperty().get() + bot.getValue());
                player.addSize(bot.getValue() / 4);
                world.removeEntity(bot);
            }
        }
    }
}