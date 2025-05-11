package splash.entities;

import javafx.beans.property.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends Fish {
    private double currentAngle = 0;
    private double velocityX = 0;
    private double velocityY = 0;
    private final Image leftTexture;
    private final Image rightTexture;

    private final IntegerProperty health = new SimpleIntegerProperty(100);
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty points = new SimpleIntegerProperty(0);
    private final IntegerProperty coins = new SimpleIntegerProperty(0);
    private final BooleanProperty movingUp = new SimpleBooleanProperty();
    private final BooleanProperty movingDown = new SimpleBooleanProperty();
    private final BooleanProperty movingLeft = new SimpleBooleanProperty();
    private final BooleanProperty movingRight = new SimpleBooleanProperty();

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

    public Player(Image leftTexture, Image rightTexture, double baseWidth, double baseHeight) {
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        this.size = 50;
        setPosition(baseWidth / 2, baseHeight / 2);
        addTag("player");
        setHitboxOffset(-size / 2, -size / 2);
    }

    public void addPoints(int points) {
        this.points.set(this.points.get() + points);
    }

    @Override
    public void update(double deltaTime) {
        double currentX = getX();
        double currentY = getY();

        velocityX = 0;
        velocityY = 0;

        if (movingLeft.get()) {
            velocityX -= 300;
            facingLeft = true;
        }
        if (movingRight.get()) {
            velocityX += 300;
            facingLeft = false;
        }
        if (movingUp.get()) {
            velocityY -= 300;
        }
        if (movingDown.get()) {
            velocityY += 300;
        }

        currentX += velocityX * deltaTime;
        currentY += velocityY * deltaTime;

        setPosition(currentX, currentY);
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

    @Override
    public void render(GraphicsContext gc) {
        double renderSize = getScaledSize();

        double targetAngle = Math.toDegrees(Math.atan2(velocityY, velocityX)) + (facingLeft ? 180 : 0);

        if (velocityX == 0 && velocityY == 0) {
            targetAngle = facingLeft ? 360 : 0;
        }

        double deltaAngle = ((targetAngle - currentAngle + 540) % 360) - 180;
        currentAngle += deltaAngle / 10;

        gc.save();
        gc.translate(x, y);
        gc.rotate(currentAngle);

        gc.drawImage(facingLeft ? leftTexture : rightTexture,
                -renderSize / 2,
                -renderSize / 2,
                renderSize,
                renderSize);

        gc.restore();
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

}