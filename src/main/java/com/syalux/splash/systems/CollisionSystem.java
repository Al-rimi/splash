package com.syalux.splash.systems;

import java.util.ArrayList;
import java.util.List;

import com.syalux.splash.data.World;
import com.syalux.splash.entities.FishEntity;
import com.syalux.splash.entities.NPCEntity;
import com.syalux.splash.entities.PlayerEntity;

public class CollisionSystem {
    private final World world;

    public CollisionSystem(World world) {
        this.world = world;
    }

    public void checkCollisions() {
        List<PlayerEntity> players = new ArrayList<>(world.getPlayers());
        List<NPCEntity> npcs = new ArrayList<>(world.getNpcs());

        for (int i = 0; i < players.size(); i++) {
            PlayerEntity a = players.get(i);
            for (int j = i + 1; j < players.size(); j++) {
                PlayerEntity b = players.get(j);
                if (checkCollision(a, b)) {
                    handleCollision(a, b);
                }
            }
            for (int j = 0; j < npcs.size(); j++) {
                NPCEntity b = npcs.get(j);
                if (checkCollision(a, b)) {
                    handleCollision(a, b);
                }
            }
        }

        for (int i = 0; i < npcs.size(); i++) {
            NPCEntity a = npcs.get(i);
            for (int j = i + 1; j < npcs.size(); j++) {
                NPCEntity b = npcs.get(j);
                if (checkCollision(a, b)) {
                    handleCollision(a, b);
                }
            }
        }

    }

    private boolean checkCollision(FishEntity a, FishEntity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double distanceSq = dx * dx + dy * dy;
        double combinedRadius = a.getRadius() + b.getRadius();
        return distanceSq <= combinedRadius * combinedRadius;
    }

    private void handleCollision(FishEntity a, FishEntity b) {
        if (a.getSize() > b.getSize()) {
            executeCollision(a, b);
        } else if (a.getSize() < b.getSize()) {
            executeCollision(b, a);
        }
    }

    private void executeCollision(FishEntity large, FishEntity small) {
        if (large instanceof PlayerEntity && small instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) large;
            player.addScore((int) small.getSize());
            player.addSize(small.getSize() * 0.01);
            small.takeDamage(player.getSize() * 0.01);
        } else if (large instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) large;
            player.addScore((int) small.getSize());
            player.addSize(small.getSize() * 0.01);
            small.die();
        } else if (small instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) small;
            player.takeDamage(large.getSize() * 0.1);
            large.takeDamage(player.getSize() * 1.1);
        } else {
            large.addSize(1);
            small.die();
        }    
    }
}