package splash.core.engine;

import javafx.geometry.Rectangle2D;
import splash.core.entities.*;
import splash.core.world.DynamicWorld;
import java.util.*;

public class CollisionSystem {
    private final Player player;
    private final DynamicWorld world;
    private final Map<String, List<GameEntity>> collisionLayers;
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

    private boolean checkCollision(GameEntity a, GameEntity b) {
        return a.getBounds().intersects(b.getBounds());
    }

    private void handleCollision(Player a, GameEntity b) {
        if (b instanceof Enemy) handlePlayerEnemyCollision(a, (Enemy) b);
        else if (b instanceof Food) handlePlayerFoodCollision(a, (Food) b);
        else if (b instanceof CollisionBlock) resolveBlockCollision(a, (CollisionBlock) b);
    }

    private void handlePlayerEnemyCollision(Player player, Enemy enemy) {
        this.player.healthProperty().set(player.healthProperty().get() - 1);
    }

    private void handlePlayerFoodCollision(Player player, Food food) {
        this.player.pointsProperty().set(player.pointsProperty().get() + food.getValue());
        world.removeEntity(food);
    }

    private void resolveBlockCollision(GameEntity entity, CollisionBlock block) {
        Rectangle2D entityBounds = entity.getBounds();
        Rectangle2D blockBounds = block.getBounds();

        double dx = (entityBounds.getMinX() + entityBounds.getWidth()/2) - 
                   (blockBounds.getMinX() + blockBounds.getWidth()/2);
        double dy = (entityBounds.getMinY() + entityBounds.getHeight()/2) - 
                   (blockBounds.getMinY() + blockBounds.getHeight()/2);

        double combinedHalfWidth = (entityBounds.getWidth() + blockBounds.getWidth())/2;
        double combinedHalfHeight = (entityBounds.getHeight() + blockBounds.getHeight())/2;

        double overlapX = combinedHalfWidth - Math.abs(dx);
        double overlapY = combinedHalfHeight - Math.abs(dy);

        if (overlapX >= overlapY) {
            if (dy > 0) {
                entity.setPosition(entity.getX(), blockBounds.getMinY() - entity.getSize()/2);
            } else {
                entity.setPosition(entity.getX(), blockBounds.getMaxY() + entity.getSize()/2);
            }
        } else {
            if (dx > 0) {
                entity.setPosition(blockBounds.getMinX() - entity.getSize()/2, entity.getY());
            } else {
                entity.setPosition(blockBounds.getMaxX() + entity.getSize()/2, entity.getY());
            }
        }
    }
}