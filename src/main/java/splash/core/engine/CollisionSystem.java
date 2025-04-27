package splash.core.engine;

import splash.core.entities.*;
import splash.core.world.DynamicWorld;
import java.util.*;

public class CollisionSystem {
    private final Player player;
    private final DynamicWorld world;
    private final Map<String, List<Fish>> collisionLayers;
    private final Set<String> playerCollisionLayers = Set.of("enemy", "block", "food");

    public CollisionSystem(Player player, DynamicWorld world) {
        this.player = player;
        this.world = world;
        this.collisionLayers = new HashMap<>();
        initCollisionLayers();
    }

    private void initCollisionLayers() {
        collisionLayers.put("enemy", new ArrayList<>());
        collisionLayers.put("food", new ArrayList<>());
        collisionLayers.put("block", new ArrayList<>());
    }

    public void checkCollisions() {
        updateCollisionLayers();
        checkPlayerCollisions();
    }

    private void updateCollisionLayers() {
        collisionLayers.forEach((layer, entities) -> entities.clear());
        world.getEntities().forEach(entity -> {
            entity.getTags().forEach(tag -> {
                if (collisionLayers.containsKey(tag)) {
                    collisionLayers.get(tag).add(entity);
                }
            });
        });
    }

    private void checkPlayerCollisions() {
        playerCollisionLayers.forEach(layer -> {
            collisionLayers.get(layer).forEach(entity -> {
                if (checkCollision(player, entity)) {
                    handleCollision(player, entity);
                }
            });
        });
    }

    private boolean checkCollision(Fish a, Fish b) {
        return a.getBounds().intersects(b.getBounds());
    }

    private void handleCollision(Player a, Fish b) {
        if (b instanceof Enemy) handlePlayerEnemyCollision(a, (Enemy) b);
        else if (b instanceof Food) handlePlayerFoodCollision(a, (Food) b);
    }

    private void handlePlayerEnemyCollision(Player player, Enemy enemy) {
        this.player.healthProperty().set(player.healthProperty().get() - 1);
    }

    private void handlePlayerFoodCollision(Player player, Food food) {
        this.player.pointsProperty().set(player.pointsProperty().get() + food.getValue());
        world.removeEntity(food);
    }
}