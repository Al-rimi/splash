package com.syalux.splash.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.syalux.splash.core.CharacterManager;

public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int highScore;
    private int coins;
    private int fishType;
    private int fishSize;
    private int fishHealth;
    private int fishSpeed;
    private Set<Integer> unlockedCharacters = new HashSet<>();

    public Profile() {
        this.playerName = "Player";
        this.highScore = 0;
        this.coins = 400;
        this.fishType = 1;

        Character initialFish = CharacterManager.getCharacter(this.fishType);
        this.fishSize = initialFish.getBaseSize();
        this.fishHealth = initialFish.getBaseHealth();
        this.fishSpeed = initialFish.getBaseSpeed();
        this.unlockedCharacters.add(initialFish.getId());
    }

    public Profile(String playerName, int highScore, int coins, int fishType, int fishSize, int fishHealth, int fishSpeed) {
        this();
        this.playerName = playerName;
        this.highScore = highScore;
        this.coins = coins;
        this.fishType = fishType;
        this.fishSize = fishSize;
        this.fishHealth = fishHealth;
        this.fishSpeed = fishSpeed;
    }

    public void unlockCharacter(int characterId) {
        this.unlockedCharacters.add(characterId);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getFishType() {
        return fishType;
    }

    public void setFishType(int fishType) {
        this.fishType = fishType;
    }

    public int getFishSize() {
        return fishSize;
    }

    public void setFishSize(int fishSize) {
        this.fishSize = fishSize;
    }

    public int getFishHealth() {
        return fishHealth;
    }

    public void setFishHealth(int fishHealth) {
        this.fishHealth = fishHealth;
    }

    public int getFishSpeed() {
        return fishSpeed;
    }

    public void setFishSpeed(int fishSpeed) {
        this.fishSpeed = fishSpeed;
    }

    public Set<Integer> getUnlockedCharacters() {
        return unlockedCharacters;
    }

    public void addUnlockedCharacter(int characterId) {
        this.unlockedCharacters.add(characterId);
    }

    public void removeUnlockedCharacter(int characterId) {
        this.unlockedCharacters.remove(characterId);
    }

    public boolean isCharacterUnlocked(int characterId) {
        return this.unlockedCharacters.contains(characterId);
    }
}