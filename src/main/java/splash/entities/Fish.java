package splash.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import java.util.HashSet;
import java.util.Set;

public abstract class Fish {
    protected double size;
    protected double x;
    protected double y;
    protected double scale = 1.0;
    protected Rectangle2D hitbox;
    protected Set<String> tags = new HashSet<>();
    protected double hitboxOffsetX = 0;
    protected double hitboxOffsetY = 0;

    public abstract void update(double deltaTime);

    public abstract void render(GraphicsContext gc);

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        updateHitbox();
    }

    public void updateScale(double scale) {
        this.scale = scale;
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
}