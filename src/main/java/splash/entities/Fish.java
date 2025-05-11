package splash.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.HashSet;
import java.util.Set;
import splash.utils.Vector2D;

public abstract class Fish {
    protected double size;
    protected double x;
    protected double y;
    protected double scale = 1.0;
    protected Rectangle2D hitbox;
    protected Set<String> tags = new HashSet<>();
    protected double hitboxOffsetX = 0;
    protected double hitboxOffsetY = 0;
    protected boolean facingLeft = false;
    protected Image leftTexture;
    protected Image rightTexture;
    protected double currentAngle = 0;
    protected double velocityX = 0;
    protected double velocityY = 0;
    protected Vector2D randomDirection = new Vector2D();
    protected double timeSinceLastDirectionChange = 0;

    public Fish(double size) {
        this.size = size;
        setHitboxOffset(-size / 2, -size / 2);
    }

    public abstract void update(double deltaTime);

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

    protected void fleeFrom(double targetX, double targetY, double speed, double smoothFactor) {
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return;

        dx /= distance;
        dy /= distance;
        applyVelocity(-dx * speed, -dy * speed, smoothFactor);
    }

    protected void pursue(double targetX, double targetY, double speed, double smoothFactor) {
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return;

        dx /= distance;
        dy /= distance;
        applyVelocity(dx * speed, dy * speed, smoothFactor);
    }

    protected void wander(double deltaTime, double directionChangeInterval, double speed, double smoothFactor) {
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange >= directionChangeInterval) {
            randomDirection.x = Math.random() * 2 - 1;
            randomDirection.y = Math.random() * 2 - 1;
            double length = Math.sqrt(randomDirection.x * randomDirection.x + randomDirection.y * randomDirection.y);
            if (length > 0) {
                randomDirection.x /= length;
                randomDirection.y /= length;
            }
            timeSinceLastDirectionChange = 0;
        }
        applyVelocity(randomDirection.x * speed, randomDirection.y * speed, smoothFactor);
    }

    private void applyVelocity(double targetVx, double targetVy, double smoothFactor) {
        velocityX += (targetVx - velocityX) * smoothFactor;
        velocityY += (targetVy - velocityY) * smoothFactor;
        if (velocityX != 0) {
            facingLeft = velocityX < 0;
        }
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        updateHitbox();
    }

    protected void updateHitbox() {
        double scaledSize = size * scale;
        hitbox = new Rectangle2D(
                x - scaledSize / 2 + hitboxOffsetX,
                y - scaledSize / 2 + hitboxOffsetY,
                scaledSize,
                scaledSize);
    }

    public void updateScale(double scale) {
        this.scale = scale;
        updateHitbox();
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setHitboxOffset(double x, double y) {
        hitboxOffsetX = x;
        hitboxOffsetY = y;
        updateHitbox();
    }

    public Rectangle2D getBounds() {
        return hitbox;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public double getSize() {
        return size;
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
}