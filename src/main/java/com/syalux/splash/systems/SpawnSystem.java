package com.syalux.splash.systems;

import java.util.Random;

import com.syalux.splash.core.Config;
import com.syalux.splash.core.ResourceManager;
import com.syalux.splash.entities.NPC;
import com.syalux.splash.entities.Player;
import com.syalux.splash.entities.StaticEntity;
import com.syalux.splash.entities.World;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class SpawnSystem {

    private final Random random = new Random();
    private final Image[] fishImages = new Image[Config.FISH_IMAGE_COUNT];
    private final Image[] mountains = new Image[Config.MOUNTAIN_IMAGE_COUNT];
    private final Image[] seaweeds = new Image[Config.SEAWEED_IMAGE_COUNT];
    private final Image[] rocks = new Image[Config.ROCK_IMAGE_COUNT];
    private final Timeline spawnTimer;
    private final World world;

    public SpawnSystem(World world){
        this.world = world;
        this.spawnTimer = new Timeline(new KeyFrame(Duration.seconds(Config.SPAWN_DURATION_SECONDS), e -> spawn()));
        this.spawnTimer.setCycleCount(Animation.INDEFINITE);

        for (int i = 0; i < Config.FISH_IMAGE_COUNT; i++) {
            fishImages[i] = ResourceManager.getFishImage(i + 1);
        }
        for (int i = 0; i < Config.MOUNTAIN_IMAGE_COUNT; i++) {
            mountains[i] = ResourceManager.getMountainImage(i + 1);
        }
        for (int i = 0; i < Config.SEAWEED_IMAGE_COUNT; i++) {
            seaweeds[i] = ResourceManager.getSeaweedImage(i + 1);
        }
        for (int i = 0; i < Config.ROCK_IMAGE_COUNT; i++) {
            rocks[i] = ResourceManager.getRockImage(i + 1);
        }
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

    public void spawnPlayer(Player player) {
        this.world.addPlayer(player);
    }

    private void spawn() {
        world.getPlayers().forEach(player -> {
            spawnEntities(player);
            cleanupDistantEntities(player);
        });
    }

    private void spawnEntities(Player player) {
        double dx = player.getVelocityX();
        double dy = player.getVelocityY();
        double length = Math.hypot(dx, dy);
        double distance = 1500 + Math.random() * Config.SPAWN_RADIUS;
        double angle = Math.random() * 2 * Math.PI;

        double dirX = (length == 0) ? Math.cos(angle) : dx / length;
        double dirY = (length == 0) ? Math.sin(angle) : dy / length;
        double spawnX = player.getX() + dirX * distance;
        double spawnY = player.getY() + dirY * distance;

        int fishType = (int) (random.nextDouble() * Config.FISH_IMAGE_COUNT);
        if (fishType == player.getCharacter()) {
            if (fishType >= Config.FISH_IMAGE_COUNT) {
                fishType -= 2;
            } else {
                fishType++;
            }
        }

        if (random.nextDouble() < Config.SPAWN_ENEMY_PROBABILITY) {
            NPC enemy = new NPC(world, spawnX, spawnY,
                    fishImages[fishType], random.nextDouble() * player.getSize() * 1.5 + player.getSize());
            world.addNpc(enemy);
        }

        if (random.nextDouble() < Config.SPAWN_FOOD_PROBABILITY) {
            NPC food = new NPC(world, spawnX, spawnY,
                    fishImages[fishType], random.nextDouble() * player.getSize() * 0.8 + player.getSize() * 0.2);
            world.addNpc(food);
        }

        if (random.nextDouble() < Config.SPAWN_MOUNTAIN_PROBABILITY) {
            Image mountain = mountains[random.nextInt(Config.MOUNTAIN_IMAGE_COUNT)];
            world.addStaticEntity(new StaticEntity(
                    spawnX * 4, spawnY * 4,
                    mountain,
                    random.nextDouble() * 2 + 1.2,
                    random.nextDouble() * 0.2 + 0.1));
        }

        if (random.nextDouble() < Config.SPAWN_ROCK_PROBABILITY) {
            Image rock = rocks[random.nextInt(Config.ROCK_IMAGE_COUNT)];
            world.addStaticEntity(new StaticEntity(
                    spawnX + random.nextDouble() * 200 - 100,
                    spawnY + random.nextDouble() * 200 - 100,
                    rock,
                    random.nextDouble() * 0.3 + 0.1,
                    random.nextDouble() * 0.6 + 0.3));
        }

        if (random.nextDouble() < Config.SPAWN_SEAWEED_PROBABILITY) {
            Image seaweed = seaweeds[random.nextInt(Config.SEAWEED_IMAGE_COUNT)];
            world.addStaticEntity(new StaticEntity(
                    spawnX + random.nextDouble() * 300 - 150,
                    spawnY + random.nextDouble() * 300 - 150,
                    seaweed,
                    random.nextDouble() * 0.4 + 0.2,
                    random.nextDouble() * 0.8 + 0.2));
        }
    }

    
    private void cleanupDistantEntities(Player player) {
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