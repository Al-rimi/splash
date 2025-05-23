package com.syalux.splash.data;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.syalux.splash.entities.NPCEntity;
import com.syalux.splash.entities.PlayerEntity;
import com.syalux.splash.entities.StaticEntity;

public class World {
    private final ConcurrentLinkedQueue<PlayerEntity> players = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<NPCEntity> npcs = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<StaticEntity> staticEntities = new ConcurrentLinkedQueue<>();

    public void addPlayer(PlayerEntity player) {
        players.add(player);
    }

    public void addNpc(NPCEntity npc) {
        npcs.add(npc);
    }

    public void addStaticEntity(StaticEntity entity) {
        staticEntities.add(entity);
    }
    
    public ConcurrentLinkedQueue<PlayerEntity> getPlayers() {
        return players;
    }

    public ConcurrentLinkedQueue<NPCEntity> getNpcs() {
        return npcs;
    }

    public ConcurrentLinkedQueue<StaticEntity> getStaticEntities() {
        return staticEntities;
    }

    public void removePlayer(PlayerEntity player) {
        players.remove(player);
    }

    public void removeEntity(NPCEntity npc) {
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