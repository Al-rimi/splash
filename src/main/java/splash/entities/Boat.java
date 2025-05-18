package splash.entities;

import javafx.scene.image.Image;
import splash.core.Config;

public class Boat extends Fish {
    public enum BehaviorType {
        ENEMY, FOOD
    }

    private final Player player;
    private final BehaviorType behavior;
    private final double detectionRadius;
    private final double movementSpeed;
    private int value;
    private final double directionChangeInterval;

    public Boat(Player player, BehaviorType behavior,
            double x, double y,
            Image leftTexture, Image rightTexture,
            double sizeMultiplier,
            double detectionRadius,
            double movementSpeed,
            double directionChangeInterval,
            int value) {
        super(player.getSize() * sizeMultiplier);
        this.player = player;
        this.behavior = behavior;
        this.detectionRadius = detectionRadius;
        this.movementSpeed = movementSpeed;
        this.directionChangeInterval = directionChangeInterval;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        this.value = value;
        setPosition(x, y);

        addTag(behavior == BehaviorType.FOOD ? "food" : "enemy");
    }

    // Factory methods for specific entity types
    public static Boat createEnemy(Player player, double x, double y,
            Image leftTexture, Image rightTexture) {
        double sizeMultiplier = Math.random() * (Config.ENEMY_MAX_SIZE_MULTIPLIER - Config.ENEMY_MIN_SIZE_MULTIPLIER)
                + Config.ENEMY_MIN_SIZE_MULTIPLIER;
        int value = (int) (sizeMultiplier * 10) + 5;

        return new Boat(
                player,
                BehaviorType.ENEMY,
                x, y,
                leftTexture, rightTexture,
                sizeMultiplier,
                Config.ENEMY_DETECTION_RADIUS,
                Config.ENEMY_MOVEMENT_SPEED,
                Config.ENEMY_DIRECTION_CHANGE_INTERVAL,
                value);
    }

    public static Boat createFood(Player player, double x, double y,
            Image leftTexture, Image rightTexture) {
        double sizeMultiplier = Math.random() * (Config.FOOD_MAX_SIZE_MULTIPLIER - Config.FOOD_MIN_SIZE_MULTIPLIER)
                + Config.FOOD_MIN_SIZE_MULTIPLIER;
        int value = (int) (sizeMultiplier * 10) + 5;

        Boat food = new Boat(
                player,
                BehaviorType.FOOD,
                x, y,
                leftTexture, rightTexture,
                sizeMultiplier,
                Config.FOOD_DETECTION_RADIUS,
                Config.FOOD_MOVEMENT_SPEED,
                Config.FOOD_DIRECTION_CHANGE_INTERVAL,
                value);

        return food;
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
        switch (behavior) {
            case FOOD:
                fleeFrom(player.getX(), player.getY(), movementSpeed, 0.1);
                break;
            case ENEMY:
                pursue(player.getX(), player.getY(), movementSpeed, 0.1);
                break;
        }
    }

    private void updatePosition(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }

    public int getValue() {
        return behavior == BehaviorType.FOOD ? value : 0;
    }

    public BehaviorType getBehaviorType() {
        return behavior;
    }
}