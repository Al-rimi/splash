package com.syalux.splash.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.syalux.splash.data.Config;
import com.syalux.splash.data.Resource;
import com.syalux.splash.data.World;
import com.syalux.splash.entities.CoinEntity;
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
    private final CameraSystem cameraSystem;

    public SpawnSystem(World world, CameraSystem cameraSystem) {
        this.world = world;
        this.cameraSystem = cameraSystem;
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
        double camX = cameraSystem.getViewCenterX();
        double camY = cameraSystem.getViewCenterY();

        List <PlayerEntity> players = new ArrayList<>(world.getPlayers());
        
        cleanupDistantEntities(camX, camY);

        if (players.isEmpty()) {
            spawnEntities(60, 1,camX, camY);
        } else {
            for (PlayerEntity player : players) {
                spawnEntities((int) player.getSize(), player.getFishType(), camX, camY);
            }
        }
    }

    private void spawnEntities(int size, int playerFishType, double camX, double camY) {
        double distance = 2000 + Math.random() * Config.SPAWN_RADIUS;
        double angle = Math.random() * 2 * Math.PI;
        
        double spawnX = camX + Math.cos(angle) * distance + Math.random() * 800 - 200;
        double spawnY = camY + Math.sin(angle) * distance + Math.random() * 800 - 200;

        int fishType = random.nextInt(Config.FISH_IMAGE_COUNT) + 1;
        if (fishType == playerFishType) {
            fishType = random.nextInt(Config.FISH_IMAGE_COUNT) + 1;
        }

        if (random.nextDouble() < Config.SPAWN_ENEMY_PROBABILITY) {
            NPCEntity enemy = new NPCEntity(world, spawnX, spawnY, fishType,
                    random.nextDouble() * (size + 10) * 1.5 + size + 10);
            world.addNpc(enemy);
        }

        if (random.nextDouble() < Config.SPAWN_FOOD_PROBABILITY) {
            NPCEntity food = new NPCEntity(world, spawnX, spawnY, fishType,
                    random.nextDouble() * (size - 10) * 0.8 + (size - 10) * 0.2);
            world.addNpc(food);
        }

        if (random.nextDouble() < Config.SPAWN_MOUNTAIN_PROBABILITY) {
            world.addStaticEntity(new StaticEntity(
                    Resource.Environment.MOUNTAIN,
                    spawnX * 4,
                    spawnY * 4,
                    random.nextInt(Config.MOUNTAIN_IMAGE_COUNT) + 1,
                    random.nextInt(2000) + 2000));
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

        if (random.nextDouble() < Config.SPAWN_COIN_PROBABILITY) {
            world.addCoin(new CoinEntity( spawnX, spawnY));
        }
    }

    private void cleanupDistantEntities(double camX, double camY) {
        world.getPlayers().removeIf(p -> {
            return p.isDead();
        });

        world.getNpcs().removeIf(npc -> {
            double dx = npc.getX() - camX;
            double dy = npc.getY() - camY;
            return (dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS) || npc.isDead();
        });

        world.getStaticEntities().removeIf(entity -> {
            double dx = entity.getX() - camX;
            double dy = entity.getY() - camY;
            return dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS * 100;
        });

        world.getCoins().removeIf(coin -> {
            double dx = coin.getX() - camX;
            double dy = coin.getY() - camY;
            return dx * dx + dy * dy > Config.DESPAWN_RADIUS * Config.DESPAWN_RADIUS;
        });
    }
}