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

    /**
     * Spawns new entities (NPCs, static environment objects, and coins)
     * into the game world based on the current camera position and player stats.
     * This method also cleans up entities that are too far from the camera.
     */
    private void spawn() {
        double camX = cameraSystem.getViewCenterX();
        double camY = cameraSystem.getViewCenterY();

        List<PlayerEntity> players = new ArrayList<>(world.getPlayers());

        cleanupDistantEntities(camX, camY);

        if (players.isEmpty()) {
            spawnEntities(60, 1, 100, camX, camY);
        } else {
            for (PlayerEntity player : players) {
                spawnEntities((int) player.getSize(), player.getFishType(), player.getScore(), camX, camY);
            }
        }
    }

    private void spawnEntities(int playerSize, int playerFishType, int playerScore, double camX, double camY) {
        double distance = 2000 + Math.random() * Config.SPAWN_RADIUS;
        double angle = Math.random() * 2 * Math.PI;

        double spawnX = camX + Math.cos(angle) * distance + Math.random() * 800 - 200;
        double spawnY = camY + Math.sin(angle) * distance + Math.random() * 800 - 200;

        double difficultyFactor = Math.log(playerScore + 2) / (5 + (25.0 * (1 - Config.GAME_DIFFICULTY_FACTOR)));
        difficultyFactor = Math.min(1.0, Math.max(0.05, difficultyFactor));

        int fishType = random.nextInt(Config.FISH_IMAGE_COUNT) + 1;
        if (fishType == playerFishType) {
            fishType = random.nextInt(Config.FISH_IMAGE_COUNT) + 1;
        }

        if (random.nextDouble() < difficultyFactor) {
            NPCEntity enemy = new NPCEntity(world, spawnX, spawnY, fishType,
                    (int) (random.nextDouble() * (playerSize + 10) * 1.5 + (playerSize + 10)), difficultyFactor);
            world.addNpc(enemy);
        }

        if (random.nextDouble() < (1.5 - difficultyFactor)) {
            NPCEntity food = new NPCEntity(world, spawnX, spawnY, fishType,
                    (int) (random.nextDouble() * (playerSize - 10) * 0.8 + (playerSize - 10) * 0.2), difficultyFactor);
            world.addNpc(food);
        }

        for (int i = 1; i < 10; i++) {
            world.addStaticEntity(new StaticEntity(
                    Resource.Environment.MOUNTAIN,
                    spawnX * 16 + random.nextDouble() * 100 + i,
                    spawnY * 16 + random.nextDouble() * 100 + i,
                    random.nextInt(Config.MOUNTAIN_IMAGE_COUNT) + 1,
                    random.nextInt(2000) + 2000));

            world.addStaticEntity(new StaticEntity(
                    Resource.Environment.ROCK,
                    spawnX * 3 + random.nextDouble() * 100 + i,
                    spawnY * 3 + random.nextDouble() * 100 + i,
                    random.nextInt(Config.ROCK_IMAGE_COUNT) + 1,
                    random.nextInt(100) + 50));

            world.addStaticEntity(new StaticEntity(
                    Resource.Environment.SEAWEED,
                    spawnX * 2 + random.nextDouble() * 100 + i,
                    spawnY * 2 + random.nextDouble() * 100 + i,
                    random.nextInt(Config.SEAWEED_IMAGE_COUNT) + 1,
                    random.nextInt(200) + 50));
        }

        if (random.nextDouble() < difficultyFactor) {
            world.addCoin(new CoinEntity(spawnX, spawnY));
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