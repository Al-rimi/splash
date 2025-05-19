package splash.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import splash.utils.Vector2D;

public abstract class Fish {
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

    protected Image texture;

    protected String tag;

    public Fish(double size) {
        this.size = size;
        setHitboxOffset(-size / 2, -size / 2);
    }

    public abstract void update(double deltaTime);

    public void render(GraphicsContext gc) {
        double renderSize = getScaledSize();
        double targetAngle = calculateTargetAngle();
        currentAngle += angleDiff(targetAngle, currentAngle) / 10;

        gc.save();
        gc.translate(x, y);
        gc.rotate(currentAngle);
        gc.setGlobalAlpha(getOpacity());

        if (facingLeft) {
            gc.scale(-1, 1);
        }

        double drawX = -renderSize / 2;
        double drawY = -renderSize / 2;

        if (facingLeft) {
            drawX = -drawX - renderSize;
        }

        gc.drawImage(texture, drawX, drawY, renderSize, renderSize);

        gc.restore();
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

    protected Image getCurrentTexture() {
        return texture;
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
            applyVelocity(-dir.x * speed, -dir.y * speed, smoothFactor);
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
        double scaledSize = getScaledSize();
        hitbox = new Rectangle2D(
                x - scaledSize / 2 + hitboxOffsetX,
                y - scaledSize / 2 + hitboxOffsetY,
                scaledSize,
                scaledSize);
    }

    public Rectangle2D getBounds() {
        return hitbox;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public double getSize() {
        return size;
    }

    public void addSize(double delta) {
        size += delta;
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
        return 1.0;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
