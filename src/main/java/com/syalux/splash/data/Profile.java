package com.syalux.splash.data;

public class Profile {
    private String playerName;
    private int highScore;
    private int coins;
    private int fishType;
    private int fishSize;
    private int fishHealth;
    private int fishSpeed;

    public Profile() {
        this.playerName = "Player";
        this.highScore = 0;
        this.coins = 0;
        this.fishType = 1;
        this.fishSize = 60;
        this.fishHealth = 1;
        this.fishSpeed = 600;
    }
    
    public Profile(String playerName, int highScore, int coins, int fishType, int fishSize, int fishHealth, int fishSpeed) {
        this.playerName = playerName;
        this.highScore = highScore;
        this.coins = coins;
        this.fishType = fishType;
        this.fishSize = fishSize;
        this.fishHealth = fishHealth;
        this.fishSpeed = fishSpeed;
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
}
