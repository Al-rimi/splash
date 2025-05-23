package com.syalux.splash.systems;

import java.util.Random;

import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;
import com.syalux.splash.data.World;
import com.syalux.splash.entities.NPCEntity;
import com.syalux.splash.entities.PlayerEntity;
import com.syalux.splash.entities.StaticEntity;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SpawnSystem {

    private final Random random = new Random();
    private final Timeline spawnTimer;
    private final World world;

    public SpawnSystem(World world){
        this.world = world;
        this.spawnTimer = new Timeline(new KeyFrame(Duration.seconds(Config.SPAWN_DURATION_SECONDS), e -> spawn()));
        this.spawnTimer.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
        spawnTimer.play();
    }

    public void stop() {
        spawnTimer.stop();
    }

    public void pause() {
        spawnTimer.pause();
    }

    public void spawnPlayer(PlayerEntity player) {
        this.world.addPlayer(player);
    }

    private void spawn() {
        world.getPlayers().forEach(player -> {
            spawnEntities(player);
            cleanupDistantEntities(player);
        });
    }

    private void spawnEntities(PlayerEntity player) {
        double dx = player.getVelocityX();
        double dy = player.getVelocityY();
        double length = Math.hypot(dx, dy);
        double distance = 1500 + Math.random() * Config.SPAWN_RADIUS;
        double angle = Math.random() * 2 * Math.PI;

        double dirX = (length == 0) ? Math.cos(angle) : dx / length;
        double dirY = (length == 0) ? Math.sin(angle) : dy / length;
        double spawnX = player.getX() + dirX * distance;
        double spawnY = player.getY() + dirY * distance;

        int fishType = random.nextInt(Config.FISH_IMAGE_COUNT) + 1;
        if (fishType == player.getFishType()) {
            fishType = random.nextInt(Config.FISH_IMAGE_COUNT) + 1;
        }

        if (random.nextDouble() < Config.SPAWN_ENEMY_PROBABILITY) {
            NPCEntity enemy = new NPCEntity(world, spawnX, spawnY, fishType, random.nextDouble() * player.getSize() * 1.5 + player.getSize());
            world.addNpc(enemy);
        }

        if (random.nextDouble() < Config.SPAWN_FOOD_PROBABILITY) {
            NPCEntity food = new NPCEntity(world, spawnX, spawnY, fishType, random.nextDouble() * player.getSize() * 0.8 + player.getSize() * 0.2);
            world.addNpc(food);
        }

        if (random.nextDouble() < Config.SPAWN_MOUNTAIN_PROBABILITY) {
            world.addStaticEntity(new StaticEntity(
                    Resource.Environment.MOUNTAIN,
                    spawnX * 2, 
                    spawnY * 2,
                    random.nextInt(Config.MOUNTAIN_IMAGE_COUNT) + 1,
                    random.nextInt(4000) + 2000));
        }

        if (random.nextDouble() < Config.SPAWN_ROCK_PROBABILITY) {
            world.addStaticEntity(new StaticEntity(
                    Resource.Environment.ROCK,
                    spawnX,
                    spawnY,
                    random.nextInt(Config.ROCK_IMAGE_COUNT) + 1,
                    random.nextInt(100) + 50));
        }

        if (random.nextDouble() < Config.SPAWN_SEAWEED_PROBABILITY) {
            world.addStaticEntity(new StaticEntity(Resource.Environment.SEAWEED, spawnX, spawnY,
                    random.nextInt(Config.SEAWEED_IMAGE_COUNT) + 1,
                    random.nextInt(200) + 50));
        }
    }

    
    private void cleanupDistantEntities(PlayerEntity player) {
        world.getPlayers().removeIf(p -> {
            return p.isDead();
        });

        world.getNpcs().removeIf(npc -> {
            double dx = npc.getX() - player.getX();
            double dy = npc.getY() - player.getY();
            return (dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS) || npc.isDead();
        });

        world.getStaticEntities().removeIf(entity -> {
            double dx = entity.getX() - player.getX();
            double dy = entity.getY() - player.getY();
            return dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS * 12;
        });
    }
}