package com.syalux.splash.data;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.syalux.splash.entities.CoinEntity;
import com.syalux.splash.entities.NPCEntity;
import com.syalux.splash.entities.PlayerEntity;
import com.syalux.splash.entities.StaticEntity;

public class World {
    private final ConcurrentLinkedQueue<PlayerEntity> players = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<NPCEntity> npcs = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<StaticEntity> staticEntities = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<CoinEntity> coins = new ConcurrentLinkedQueue<>();

    public void addPlayer(PlayerEntity player) {
        players.add(player);
    }

    public void addNpc(NPCEntity npc) {
        npcs.add(npc);
    }

    public void addStaticEntity(StaticEntity entity) {
        staticEntities.add(entity);
    }

    public void addCoin(CoinEntity coin) {
        coins.add(coin);
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

    public ConcurrentLinkedQueue<CoinEntity> getCoins() {
        return coins;
    }

    public void remove(PlayerEntity player) {
        players.remove(player);
    }

    public void remove(NPCEntity npc) {
        npcs.remove(npc);
    }

    public void remove(StaticEntity entity) {
        staticEntities.remove(entity);
    }

    public void remove(CoinEntity coin) {
        coins.remove(coin);
    }

    public void clear() {
        players.clear();
        npcs.clear();
        staticEntities.clear();
        coins.clear();
    }
}