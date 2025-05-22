package com.syalux.splash.entities;

import java.util.concurrent.ConcurrentLinkedQueue;

public class World {
    private final ConcurrentLinkedQueue<Player> players = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<NPC> npcs = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<StaticEntity> staticEntities = new ConcurrentLinkedQueue<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addNpc(NPC npc) {
        npcs.add(npc);
    }

    public void addStaticEntity(StaticEntity entity) {
        staticEntities.add(entity);
    }
    
    public ConcurrentLinkedQueue<Player> getPlayers() {
        return players;
    }

    public ConcurrentLinkedQueue<NPC> getNpcs() {
        return npcs;
    }

    public ConcurrentLinkedQueue<StaticEntity> getStaticEntities() {
        return staticEntities;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void removeEntity(NPC npc) {
        npcs.remove(npc);
    }

    public void removeStaticEntity(StaticEntity entity) {
        staticEntities.remove(entity);
    }

    public void clear() {
        players.clear();
        npcs.clear();
        staticEntities.clear();
    }
}