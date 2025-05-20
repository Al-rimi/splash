package splash.entities;

import javafx.scene.image.Image;
import java.util.Random;

public class NPC extends Fish {
    private final World world;
    private int detectionRadius;
    private int movementSpeed;
    private double directionChangeInterval;
    private int intelligenceLevel;

    public NPC(World world, double x, double y, Image texture, double size) {
        super(size, new Random().nextInt((int) size) + 10, x, y, texture);

        this.world = world;
        this.intelligenceLevel = new Random().nextInt(3) + 1;

        this.detectionRadius = new Random().nextInt(2000) + 500;
        this.movementSpeed = new Random().nextInt(600) + 200;
        this.directionChangeInterval = new Random().nextDouble() * 0.5 + 0.5;
        setPosition(x, y);
    }

    @Override
    public void update(double deltaTime) {
        switch (intelligenceLevel) {
            case 3 -> advanced(deltaTime);
            case 2 -> intermediate(deltaTime);
            default -> basic(deltaTime);
        }
        updatePosition(deltaTime);
    }

    private void basic(double deltaTime) {
        Fish nearestEnemy = findNearestEntity(true);
        Fish nearestFood = findNearestEntity(false);

        if (nearestEnemy != null) {
            fleeFrom(nearestEnemy.getX(), nearestEnemy.getY(), movementSpeed, 0.1);
        } else if (nearestFood != null) {
            pursue(nearestFood.getX(), nearestFood.getY(), movementSpeed, 0.1);
        } else {
            wander(deltaTime, directionChangeInterval, movementSpeed, 0.05);
        }
    }

    private void intermediate(double deltaTime) {
        Fish nearestEnemy = findNearestEntity(true);
        Fish bestFood = findBestFood();

        if (nearestEnemy != null) {
            fleeFrom(nearestEnemy.getX(), nearestEnemy.getY(), movementSpeed * 1.1, 0.1);
        } else if (bestFood != null) {
            pursue(bestFood.getX(), bestFood.getY(), movementSpeed * 1.1, 0.1);
        } else {
            wander(deltaTime, directionChangeInterval * 0.8, movementSpeed, 0.05);
        }
    }

    private void advanced(double deltaTime) {
        Fish nearestEnemy = findNearestEntity(true);
        Fish bestFood = findBestFood();
        Fish player = world.getPlayer();


        if (player != null && player.getSize() > size) {
            fleeFrom(player.getX(), player.getY(), movementSpeed * 1.2, 0.1);
        } else if (player != null && player.getSize() < size) {
            pursue(player.getX(), player.getY(), movementSpeed * 1.2, 0.1);
        } else if (nearestEnemy != null) {
            fleeFrom(nearestEnemy.getX(), nearestEnemy.getY(), movementSpeed * 1.2, 0.1);
        } else if (bestFood != null) {
            pursue(bestFood.getX(), bestFood.getY(), movementSpeed * 1.2, 0.1);
        } else {
            wander(deltaTime, directionChangeInterval * 0.5, movementSpeed, 0.05);
        }
    }

    private Fish findBestFood() {
        Fish bestFood = null;
        double bestValue = Double.MIN_VALUE;
        
        for (Fish entity : world.getEntities()) {
            if (entity == this || entity.getSize() >= this.size) continue;
            
            double dx = entity.getX() - x;
            double dy = entity.getY() - y;
            double distance = Math.hypot(dx, dy);
            double value = entity.getSize() / (distance + 1);
            
            if (distance < detectionRadius && value > bestValue) {
                bestFood = entity;
                bestValue = value;
            }
        }
        return bestFood;
    }

    private Fish findNearestEntity(boolean larger) {
        Fish nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Fish entity : world.getEntities()) {
            if (entity == this) continue;
            
            double dx = entity.getX() - x;
            double dy = entity.getY() - y;
            double distance = Math.hypot(dx, dy);
            
            if (distance < detectionRadius && (larger ? entity.getSize() > size : entity.getSize() < size)) {
                if (distance < nearestDistance) {
                    nearest = entity;
                    nearestDistance = distance;
                }
            }
        }
        return nearest;
    }

    private void updatePosition(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }
}