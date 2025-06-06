package com.syalux.splash.entities;

import com.syalux.splash.data.Config;
import com.syalux.splash.data.Profile;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PlayerEntity extends FishEntity {
    private final IntegerProperty score;
    private final IntegerProperty coins;
    private final int initialMaxHealth;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;

    public PlayerEntity(Profile profile) {
        super(profile.getFishSize(), profile.getFishSpeed(), profile.getFishHealth(), Config.GAME_HEIGHT / 2, Config.GAME_HEIGHT / 2, profile.getFishType());
        this.isPlayer = true;
        this.score = new SimpleIntegerProperty(0);
        this.coins = new SimpleIntegerProperty(profile.getCoins());
        this.initialMaxHealth = profile.getFishHealth(); // Store the initial max health from the profile
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        if (isDead) return;

        updateVelocity();
        updatePosition(deltaTime);
    }

    private void updateVelocity() {
        velocityX = calculateVelocity(movingLeft, movingRight);
        velocityY = calculateVelocity(movingUp, movingDown);

        if (velocityX != 0) {
            facingLeft = velocityX < 0;
        }
    }

    private double calculateVelocity(boolean negativeDirection, boolean positiveDirection) {
        return (negativeDirection ? -speed : 0) + (positiveDirection ? speed : 0);
    }

    private void updatePosition(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }

    public IntegerProperty coinsProperty() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins.set(this.coins.get() + coins);
    }

    public int getCoins() {
        return coins.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void addScore(int score) {
        this.score.set(this.score.get() + score);
    }

    public int getScore() {
        return score.get();
    }

    public int getInitialMaxHealth() {
        return initialMaxHealth;
    }

    public void moveUp(boolean moving) {
        movingUp = moving;
    }

    public void moveDown(boolean moving) {
        movingDown = moving;
    }

    public void moveLeft(boolean moving) {
        movingLeft = moving;
    }

    public void moveRight(boolean moving) {
        movingRight = moving;
    }
}
