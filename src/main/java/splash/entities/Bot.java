package splash.entities;

import javafx.scene.image.Image;
import splash.core.Config;

public class Bot extends Fish {

    private final Player player;
    private final double detectionRadius;
    private final double movementSpeed;
    private int value;
    private final double directionChangeInterval;

    public Bot(Player player,
            double x, double y,
            Image texture,
            double sizeMultiplier,
            double detectionRadius,
            double movementSpeed,
            double directionChangeInterval,
            int value) {
        super(player.getSize() * sizeMultiplier);
        
        this.player = player;
        this.detectionRadius = detectionRadius;
        this.movementSpeed = movementSpeed;
        this.directionChangeInterval = directionChangeInterval;
        this.texture = texture;
        this.value = value;
        setPosition(x, y);
        setTag("bot");
    }

    // Factory methods for specific entity types
    public static Bot createEnemy(Player player, double x, double y,
            Image texture) {
        double sizeMultiplier = Math.random() * (Config.ENEMY_MAX_SIZE_MULTIPLIER - Config.ENEMY_MIN_SIZE_MULTIPLIER)
                + Config.ENEMY_MIN_SIZE_MULTIPLIER;
        int value = (int) (sizeMultiplier * 10) + 5;

        return new Bot(
                player,
                x, y,
                texture,
                sizeMultiplier,
                Config.ENEMY_DETECTION_RADIUS,
                Config.ENEMY_MOVEMENT_SPEED,
                Config.ENEMY_DIRECTION_CHANGE_INTERVAL,
                value);
    }

    public static Bot createFood(Player player, double x, double y,
            Image texture) {
        double sizeMultiplier = Math.random() * (Config.FOOD_MAX_SIZE_MULTIPLIER - Config.FOOD_MIN_SIZE_MULTIPLIER)
                + Config.FOOD_MIN_SIZE_MULTIPLIER;
        int value = (int) (sizeMultiplier * 10) + 5;

        return new Bot(
                player,
                x, y,
                texture,
                sizeMultiplier,
                Config.FOOD_DETECTION_RADIUS,
                Config.FOOD_MOVEMENT_SPEED,
                Config.FOOD_DIRECTION_CHANGE_INTERVAL,
                value);
    }

    @Override
    public void update(double deltaTime) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.hypot(dx, dy);

        if (distance <= detectionRadius) {
            handleTargetBehavior();
        } else {
            wander(deltaTime, directionChangeInterval, movementSpeed, 0.05);
        }

        updatePosition(deltaTime);
    }

    private void handleTargetBehavior() {
        if (this.size <= player.getSize()) {
            fleeFrom(player.getX(), player.getY(), movementSpeed, 0.1);
        } else {
            pursue(player.getX(), player.getY(), movementSpeed, 0.1);
        }
    }

    private void updatePosition(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }

    public int getValue() {
        return value;
    }
}