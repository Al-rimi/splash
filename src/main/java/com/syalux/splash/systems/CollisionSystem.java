package com.syalux.splash.systems;

import java.util.ArrayList;
import java.util.List;

import com.syalux.splash.entities.Fish;
import com.syalux.splash.entities.NPC;
import com.syalux.splash.entities.Player;
import com.syalux.splash.entities.World;

public class CollisionSystem {
    private final World world;

    public CollisionSystem(World world) {
        this.world = world;
    }

    public void checkCollisions() {
        List<Player> players = new ArrayList<>(world.getPlayers());
        List<NPC> npcs = new ArrayList<>(world.getNpcs());

        for (int i = 0; i < players.size(); i++) {
            Player a = players.get(i);
            for (int j = i + 1; j < players.size(); j++) {
                Player b = players.get(j);
                if (checkCollision(a, b)) {
                    handleCollision(a, b);
                }
            }
            for (int j = 0; j < npcs.size(); j++) {
                NPC b = npcs.get(j);
                if (checkCollision(a, b)) {
                    handleCollision(a, b);
                }
            }
        }

        for (int i = 0; i < npcs.size(); i++) {
            NPC a = npcs.get(i);
            for (int j = i + 1; j < npcs.size(); j++) {
                NPC b = npcs.get(j);
                if (checkCollision(a, b)) {
                    handleCollision(a, b);
                }
            }
        }

    }

    private boolean checkCollision(Fish a, Fish b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double distanceSq = dx * dx + dy * dy;
        double combinedRadius = a.getRadius() + b.getRadius();
        return distanceSq <= combinedRadius * combinedRadius;
    }

    private void handleCollision(Fish a, Fish b) {
        if (a.getSize() > b.getSize()) {
            executeCollision(a, b);
        } else if (a.getSize() < b.getSize()) {
            executeCollision(b, a);
        }
    }

    private void executeCollision(Fish large, Fish small) {
        if (large instanceof Player && small instanceof Player) {
            Player player = (Player) large;
            player.addScore((int) small.getSize());
            player.addSize(small.getSize() * 0.01);
            small.takeDamage(player.getSize() * 0.01);
        } else if (large instanceof Player) {
            Player player = (Player) large;
            player.addScore((int) small.getSize());
            player.addSize(small.getSize() * 0.01);
            small.die();
        } else if (small instanceof Player) {
            Player player = (Player) small;
            player.takeDamage(large.getSize() * 0.1);
            large.takeDamage(player.getSize() * 1.1);
        } else {
            large.addSize(1);
            small.die();
        }    
    }
}