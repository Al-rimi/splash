package splash.entities;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class Player extends Fish {
    private final IntegerProperty health = new SimpleIntegerProperty(100);
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty points = new SimpleIntegerProperty(0);
    private final IntegerProperty coins = new SimpleIntegerProperty(0);
    private final BooleanProperty movingUp = new SimpleBooleanProperty();
    private final BooleanProperty movingDown = new SimpleBooleanProperty();
    private final BooleanProperty movingLeft = new SimpleBooleanProperty();
    private final BooleanProperty movingRight = new SimpleBooleanProperty();

    public Player(Image leftTexture, Image rightTexture, double baseWidth, double baseHeight) {
        super(50);
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        setPosition(baseWidth / 2, baseHeight / 2);
        addTag("player");
    }

    @Override
    public void update(double deltaTime) {
        velocityX = (movingLeft.get() ? -300 : 0) + (movingRight.get() ? 300 : 0);
        velocityY = (movingUp.get() ? -300 : 0) + (movingDown.get() ? 300 : 0);
        if (velocityX != 0) {
            facingLeft = velocityX < 0;
        }

        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public IntegerProperty coinsProperty() {
        return coins;
    }

    public void addPoints(int points) {
        this.points.set(this.points.get() + points);
    }

    public void moveUp(boolean moving) {
        movingUp.set(moving);
    }

    public void moveDown(boolean moving) {
        movingDown.set(moving);
    }

    public void moveLeft(boolean moving) {
        movingLeft.set(moving);
    }

    public void moveRight(boolean moving) {
        movingRight.set(moving);
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

}