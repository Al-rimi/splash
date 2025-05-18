package splash.entities;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.util.Duration;
import splash.core.Config;

public class Player extends Fish {

    private final IntegerProperty health = new SimpleIntegerProperty(100);
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty points = new SimpleIntegerProperty(0);
    private final IntegerProperty coins = new SimpleIntegerProperty(0);
    private final BooleanProperty invulnerable = new SimpleBooleanProperty(false);
    private final DoubleProperty opacity = new SimpleDoubleProperty(1.0);
    private final int character;

    private final BooleanProperty movingUp = new SimpleBooleanProperty();
    private final BooleanProperty movingDown = new SimpleBooleanProperty();
    private final BooleanProperty movingLeft = new SimpleBooleanProperty();
    private final BooleanProperty movingRight = new SimpleBooleanProperty();

    public Player(int character, Image texture, double baseWidth, double baseHeight) {
        super(Config.PLAYER_BASE_SIZE);
        this.texture = texture;
        this.character = character;
        setPosition(baseWidth / 2, baseHeight / 2);
        addTag("player");
    }

    @Override
    public void update(double deltaTime) {
        updateVelocity();
        updatePosition(deltaTime);
    }

    public void startDamageAnimation() {
        if (invulnerable.get())
            return;
        invulnerable.set(true);
        opacity.set(0.5);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.15), e -> {
                    opacity.set(opacity.get() == 0.5 ? 1.0 : 0.5);
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            timeline.stop();
            invulnerable.set(false);
            opacity.set(1.0);
        });
        delay.play();
    }

    private void updateVelocity() {
        velocityX = calculateVelocity(movingLeft.get(), movingRight.get());
        velocityY = calculateVelocity(movingUp.get(), movingDown.get());

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

    public int getCharacter() {
        return character;
    }

    @Override
    public double getOpacity() {
        return opacity.get();
    }

    public boolean isInvulnerable() {
        return invulnerable.get();
    }
}
