package com.syalux.splash.entities;

import com.syalux.splash.data.Resource;
import com.syalux.splash.utils.Vector2D;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public abstract class FishEntity {
    protected double size;
    protected double scale = 1.0;
    protected boolean facingLeft = false;

    protected double x, y;
    protected double velocityX = 0;
    protected double velocityY = 0;
    protected double currentAngle = 0;

    protected Rectangle2D hitbox;
    protected double hitboxOffsetX = 0;
    protected double hitboxOffsetY = 0;

    protected Vector2D randomDirection = new Vector2D();
    protected double timeSinceLastDirectionChange = 0;
    protected double opacity = 1.0;
    protected boolean invulnerable = false;
    protected final IntegerProperty health;
    protected int speed;

    protected FishEntity killer;
    protected int fishType;
    protected boolean isPlayer;
    protected boolean isDead = false;

    private Timeline damageAnimationTimeline;
    private Timeline movingAnimationTimeline;
    private boolean isMoving = false;
    private double movingScaleOffset = 0;
    private double movingAngleOffset = 0;
    private double deathRotationSpeed = 0;
    private double deathFadeOutSpeed = 0.5;
    private double deathVerticalDrift = 100;

    public FishEntity(double size, int speed, int health, double x, double y, int fishType) {
        this.size = size;
        this.speed = speed;
        this.health = new SimpleIntegerProperty(health);
        this.x = x;
        this.y = y;
        this.fishType = fishType;
        this.isPlayer = false;
        setHitboxOffset(0, 0);

        setupMovingAnimation();
    }

    /**
     * Sets up the subtle moving animation for the fish.
     * This animation adjusts the fish's scale and angle slightly to simulate swimming.
     */
    private void setupMovingAnimation() {
        if (movingAnimationTimeline != null) {
            movingAnimationTimeline.stop();
        }
        movingAnimationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.0), e -> {
                    movingScaleOffset = 0;
                    movingAngleOffset = 0;
                }),
                new KeyFrame(Duration.seconds(0.2), e -> {
                    movingScaleOffset = 0.02;
                    movingAngleOffset = 2;
                }),
                new KeyFrame(Duration.seconds(0.4), e -> {
                    movingScaleOffset = 0;
                    movingAngleOffset = 0;
                }),
                new KeyFrame(Duration.seconds(0.6), e -> {
                    movingScaleOffset = -0.02;
                    movingAngleOffset = -2;
                }),
                new KeyFrame(Duration.seconds(0.8), e -> {
                    movingScaleOffset = 0;
                    movingAngleOffset = 0;
                })
        );
        movingAnimationTimeline.setCycleCount(Animation.INDEFINITE);
    }

    public void update(double deltaTime) {
        if (isDead) {
            y -= deathVerticalDrift * deltaTime;
            currentAngle += deathRotationSpeed * deltaTime;
            opacity -= deathFadeOutSpeed * deltaTime;

            if (opacity < 0) {
                opacity = 0;
            }
            if (opacity == 0) {
                return;
            }
        }

        boolean currentlyMoving = (Math.abs(velocityX) > 0.1 || Math.abs(velocityY) > 0.1);
        if (currentlyMoving && !isMoving) {
            isMoving = true;
            if (movingAnimationTimeline.getStatus() != Animation.Status.RUNNING) {
                movingAnimationTimeline.play();
            }
        } else if (!currentlyMoving && isMoving) {
            isMoving = false;
            movingAnimationTimeline.stop();
            movingScaleOffset = 0;
            movingAngleOffset = 0;
        }
    }

    public void render(GraphicsContext gc) {
        double renderSize = getScaledSize();
        double targetAngle = calculateTargetAngle();
        currentAngle += angleDiff(targetAngle, currentAngle) / 10;

        gc.save();
        gc.translate(x, y);

        if (isMoving) {
            gc.rotate(currentAngle + movingAngleOffset);
        } else {
            gc.rotate(currentAngle);
        }

        gc.setGlobalAlpha(getOpacity());

        if (facingLeft) {
            gc.scale(-1, 1);
        }

        double drawX = -renderSize / 2;
        double drawY = -renderSize / 2;

        if (facingLeft) {
            drawX = -drawX - renderSize;
        }

        double finalRenderSize = renderSize * (1 + movingScaleOffset);
        gc.drawImage(Resource.getFishImage(fishType), drawX - (finalRenderSize - renderSize) / 2, drawY - (finalRenderSize - renderSize) / 2, finalRenderSize, finalRenderSize);

        gc.restore();
    }

    public void debugDrawHitbox(GraphicsContext gc) {
        gc.save();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        double radius = getRadius();
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        gc.restore();
    }

    /**
     * Handles the fish taking damage, applying a visual flicker and temporary invulnerability.
     *
     * @param damage The amount of damage taken.
     * @param source The entity that dealt the damage.
     */
    public void takeDamage(double damage, FishEntity source) {
        if (invulnerable || isDead)
            return;

        health.set(health.get() - (int) damage);
        invulnerable = true;

        if (damageAnimationTimeline != null) {
            damageAnimationTimeline.stop();
        }

        damageAnimationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> {
                    opacity = 0.5;
                    scale = 0.95;
                }),
                new KeyFrame(Duration.seconds(0.2), e -> {
                    opacity = 1.0;
                    scale = 1.05;
                }),
                new KeyFrame(Duration.seconds(0.3), e -> {
                    opacity = 0.5;
                    scale = 0.95;
                }),
                new KeyFrame(Duration.seconds(0.4), e -> {
                    opacity = 1.0;
                    scale = 1.0;
                })
        );
        damageAnimationTimeline.setCycleCount(2);
        damageAnimationTimeline.play();

        PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
        delay.setOnFinished(e -> {
            invulnerable = false;
            opacity = 1.0;
            scale = 1.0;
            damageAnimationTimeline.stop();
        });
        delay.play();

        if (health.get() <= 0) {
            die(source);
        }
    }

    /**
     * Initiates the death animation for the fish.
     * The fish will rotate, drift upwards, and fade out.
     *
     * @param killer The entity that killed this fish.
     */
    protected void die(FishEntity killer) {
        if (isDead) return;

        health.set(0);
        isDead = true;
        this.killer = killer;

        if (damageAnimationTimeline != null) damageAnimationTimeline.stop();
        if (movingAnimationTimeline != null) movingAnimationTimeline.stop();

        deathRotationSpeed = (Math.random() > 0.5 ? 1 : -1) * 360;
        deathFadeOutSpeed = 0.8;
        deathVerticalDrift = 150;
    }

    public FishEntity getKiller() {
        return killer;
    }

    protected double calculateTargetAngle() {
        if (velocityX == 0 && velocityY == 0)
            return facingLeft ? 360 : 0;
        double angle = Math.toDegrees(Math.atan2(velocityY, velocityX));
        return facingLeft ? angle + 180 : angle;
    }

    protected double angleDiff(double target, double current) {
        double delta = (target - current + 540) % 360 - 180;
        return delta;
    }

    protected void applyVelocity(double targetVx, double targetVy, double smoothFactor) {
        velocityX += (targetVx - velocityX) * smoothFactor;
        velocityY += (targetVy - velocityY) * smoothFactor;
        if (velocityX != 0)
            facingLeft = velocityX < 0;
    }

    protected void fleeFrom(double targetX, double targetY, double speed, double smoothFactor) {
        Vector2D dir = new Vector2D(targetX - x, targetY - y);
        if (normalizeVector(dir))
            applyVelocity(-dir.x * speed * 2, -dir.y * speed * 2, smoothFactor);
    }

    protected void pursue(double targetX, double targetY, double speed, double smoothFactor) {
        Vector2D dir = new Vector2D(targetX - x, targetY - y);
        if (normalizeVector(dir))
            applyVelocity(dir.x * speed, dir.y * speed, smoothFactor);
    }

    protected void wander(double deltaTime, double directionChangeInterval, double speed, double smoothFactor) {
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange >= directionChangeInterval) {
            randomDirection.x = Math.random() * 2 - 1;
            randomDirection.y = Math.random() * 2 - 1;
            normalizeVector(randomDirection);
            timeSinceLastDirectionChange = 0;
        }
        applyVelocity(randomDirection.x * speed, randomDirection.y * speed, smoothFactor);
    }

    protected boolean normalizeVector(Vector2D v) {
        double length = Math.hypot(v.x, v.y);
        if (length == 0)
            return false;
        v.x /= length;
        v.y /= length;
        return true;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        updateHitbox();
    }

    public void updateScale(double scale) {
        this.scale = scale;
        updateHitbox();
    }

    public void setHitboxOffset(double x, double y) {
        hitboxOffsetX = x;
        hitboxOffsetY = y;
        updateHitbox();
    }

    protected void updateHitbox() {
        double scaledSize = getScaledSize() * 0.8;
        hitbox = new Rectangle2D(
                x - scaledSize / 2 + hitboxOffsetX,
                y - scaledSize / 2 + hitboxOffsetY,
                scaledSize, scaledSize);
    }

    public Rectangle2D getBounds() {
        return hitbox;
    }

    public double getRadius() {
        return getScaledSize() * 0.4;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public int getHealth() {
        return health.get();
    }

    public double getSize() {
        return size;
    }

    public void addSize(double size) {
        this.size += size;
        updateHitbox();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getScaledSize() {
        return size * scale;
    }

    public double getOpacity() {
        return opacity;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    public void addhealth(int health) {
        this.health.set(this.health.get() + health);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public int getSpeed() {
        return speed;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public int getFishType() {
        return fishType;
    }
}