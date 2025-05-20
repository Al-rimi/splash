package splash.systems;

import splash.entities.Fish;
import splash.entities.Player;
import splash.entities.World;
import java.util.ArrayList;
import java.util.List;

public class CollisionSystem {
    private final Player player;
    private final World world;

    public CollisionSystem(Player player, World world) {
        this.player = player;
        this.world = world;
    }

    public void checkCollisions() {
        checkPlayerCollisions();
        checkAICollisions();
    }

    private void checkPlayerCollisions() {
        world.getEntities().forEach(entity -> {
            if (checkCollision(player, entity)) {
                handleCollision(player, entity);
            }
        });
    }

    private void checkAICollisions() {
        List<Fish> entities = new ArrayList<>(world.getEntities());
        for (int i = 0; i < entities.size(); i++) {
            Fish a = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Fish b = entities.get(j);
                if (checkCollision(a, b)) {
                    handleCollision(a, b);
                }
            }
        }
    }

    private boolean checkCollision(Fish a, Fish b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double distanceSq = dx * dx + dy * dy;
        double combinedRadius = a.getRadius() + b.getRadius();
        return distanceSq <= combinedRadius * combinedRadius;
    }

    private void handleCollision(Fish a, Fish b) {
        if (a instanceof Player) {
            handlePlayerCollision((Player) a, b);
        } else if (b instanceof Player) {
            handlePlayerCollision((Player) b, a);
        } else {
            handleAICollision(a, b);
        }
    }

    private void handlePlayerCollision(Player player, Fish other) {
        if (player.getSize() > other.getSize()) {
            player.addScore((int) other.getSize());
            player.addSize(other.getSize() * 0.1);
            other.die();
        } else if (other.getSize() > player.getSize() && !player.isInvulnerable()) {
            player.takeDamage(other.getSize() * 0.2);
            other.takeDamage(player.getSize() * 2);
        }
    }

    private void handleAICollision(Fish a, Fish b) {
        if (a.getSize() > b.getSize()) {
            a.addSize(b.getSize() / 4);
            b.die();
        } else if (b.getSize() > a.getSize()) {
            b.addSize(a.getSize() / 4);
            a.die();
        }
    }
}