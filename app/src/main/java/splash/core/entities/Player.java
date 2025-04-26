package splash.core.entities;

import javafx.beans.property.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import splash.core.utils.Vector2D;

public class Player extends GameEntity {
    private final Image texture;
    private final BooleanProperty movingUp = new SimpleBooleanProperty();
    private final BooleanProperty movingDown = new SimpleBooleanProperty();
    private final BooleanProperty movingLeft = new SimpleBooleanProperty();
    private final BooleanProperty movingRight = new SimpleBooleanProperty();
    
    public Player(Image texture) {
        this.texture = texture;
        this.size = 50;
        this.x = 640;
        this.y = 360;
    }
    
    public void update(double deltaTime) {
        if(movingUp.get()) y -= 300 * deltaTime;
        if(movingDown.get()) y += 300 * deltaTime;
        if(movingLeft.get()) x -= 300 * deltaTime;
        if(movingRight.get()) x += 300 * deltaTime;
    }
    
    public void moveUp(boolean moving) { movingUp.set(moving); }
    public void moveDown(boolean moving) { movingDown.set(moving); }
    public void moveLeft(boolean moving) { movingLeft.set(moving); }
    public void moveRight(boolean moving) { movingRight.set(moving); }
    
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, size * scale, size * scale);
    }
    
    public void render() {
        // Implementation would go here
    }
}