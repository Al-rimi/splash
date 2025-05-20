package splash.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import splash.core.Config;

public class Player extends Fish {
    private final int character;
    private final IntegerProperty score;
    private final IntegerProperty coins;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;

    public Player(int character, double baseWidth, double baseHeight, Image texture) {
        super(Config.PLAYER_BASE_SIZE, 100, baseWidth / 2, baseHeight / 2, texture);
        this.isPlayer = true;
        this.character = character;
        this.score = new SimpleIntegerProperty(0);
        this.coins = new SimpleIntegerProperty(0);
    }

    @Override
    public void update(double deltaTime) {
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
        return (negativeDirection ? -Config.PLAYER_MAX_SPEED : 0) + (positiveDirection ? Config.PLAYER_MAX_SPEED : 0);
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

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void addScore(int score) {
        this.score.set(this.score.get() + score);
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

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public int getCharacter() {
        return character;
    }
}
