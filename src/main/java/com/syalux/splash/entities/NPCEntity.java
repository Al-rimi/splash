package com.syalux.splash.entities;

import java.util.Random;

import com.syalux.splash.data.World;

public class NPCEntity extends FishEntity {
    private final World world;
    private int detectionRadius;
    private double directionChangeInterval;
    private int intelligenceLevel;

    public NPCEntity(World world, double x, double y, int fishType, int size, double difficultyFactor) {
        super(size, new Random().nextInt(300) + 200 + (int) (400 * difficultyFactor), new Random().nextInt(size) + size, x, y, fishType);
        this.world = world;

        this.intelligenceLevel = (int) Math.min(3, Math.max(1, difficultyFactor * 3));
        this.detectionRadius = (int) (difficultyFactor * 4000 + 500);
        this.directionChangeInterval = Math.max(0.3, 1.0 - difficultyFactor);

        setPosition(x, y);
    }

    @Override
    public void update(double deltaTime) {
        switch (intelligenceLevel) {
            case 3: 
                advanced(deltaTime);
                break;
            case 2: intermediate(deltaTime);
                break;
            default: 
                basic(deltaTime);
                break;
        }
        updatePosition(deltaTime);
    }

    private void basic(double deltaTime) {
        FishEntity player = findNearestPlayer();
        FishEntity nearestEnemy = findNearestEntity(true);
        FishEntity nearestFood = findNearestEntity(false);

        if (player != null && player.size < size) {
            pursue(player.getX(), player.getY(), speed, 0.1);
        } else if (player != null) {
            fleeFrom(player.getX(), player.getY(), speed, 0.1);
        }

        if (nearestEnemy != null) {
            fleeFrom(nearestEnemy.getX(), nearestEnemy.getY(), speed, 0.1);
        } else if (nearestFood != null) {
            pursue(nearestFood.getX(), nearestFood.getY(), speed, 0.1);
        } else {
            wander(deltaTime, directionChangeInterval, speed, 0.05);
        }
    }

    private void intermediate(double deltaTime) {
        FishEntity player = findNearestPlayer();
        FishEntity nearestEnemy = findNearestEntity(true);
        FishEntity bestFood = findBestFood();

        if (player != null && player.size < size) {
            pursue(player.getX(), player.getY(), speed * 1.1, 0.1);
        } else if (player != null) {
            fleeFrom(player.getX(), player.getY(), speed * 1.1, 0.1);
        }

        if (nearestEnemy != null) {
            fleeFrom(nearestEnemy.getX(), nearestEnemy.getY(), speed * 1.1, 0.1);
        } else if (bestFood != null) {
            pursue(bestFood.getX(), bestFood.getY(), speed * 1.1, 0.1);
        } else {
            wander(deltaTime, directionChangeInterval * 0.8, speed, 0.05);
        }
    }

    private void advanced(double deltaTime) {
        FishEntity nearestEnemy = findNearestEntity(true);
        FishEntity player = findNearestPlayer();

        if (player != null && player.size < size) {
            pursue(player.getX(), player.getY(), speed * 1.2, 0.1);
        } else if (player != null) {
            fleeFrom(player.getX(), player.getY(), speed * 1.2, 0.1);
        }

        if (nearestEnemy != null) {
            fleeFrom(nearestEnemy.getX(), nearestEnemy.getY(), speed * 1.2, 0.1);
        } else {
            wander(deltaTime, directionChangeInterval * 0.5, speed, 0.05);
        }
    }

    private FishEntity findBestFood() {
        FishEntity bestFood = null;
        double bestValue = Double.MIN_VALUE;
        
        for (FishEntity entity : world.getNpcs()) {
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

    private FishEntity findNearestEntity(boolean larger) {
        FishEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (FishEntity entity : world.getNpcs()) {
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

    private FishEntity findNearestPlayer() {
        FishEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (FishEntity player : world.getPlayers()) {
            double dx = player.getX() - x;
            double dy = player.getY() - y;
            double distance = Math.hypot(dx, dy);

            if (distance < detectionRadius) {
                if (distance < nearestDistance) {
                    nearest = player;
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