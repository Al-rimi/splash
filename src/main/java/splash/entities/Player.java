package splash.entities;

import javafx.beans.property.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends Fish {
    private final IntegerProperty health = new SimpleIntegerProperty(100);
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty points = new SimpleIntegerProperty(0);
    private final IntegerProperty coins = new SimpleIntegerProperty(0);
    private final Image texture;
    private final BooleanProperty movingUp = new SimpleBooleanProperty();
    private final BooleanProperty movingDown = new SimpleBooleanProperty();
    private final BooleanProperty movingLeft = new SimpleBooleanProperty();
    private final BooleanProperty movingRight = new SimpleBooleanProperty();

    public IntegerProperty healthProperty() { return health; }
    public IntegerProperty levelProperty() { return level; }
    public IntegerProperty pointsProperty() { return points; }
    public IntegerProperty coinsProperty() { return coins; }

    
    public Player(Image texture) {
        this.texture = texture;
        this.size = 50;
        setPosition(640, 360);
        this.x = 640;
        this.y = 360;
        addTag("player");
        setHitboxOffset(-size/2, -size/2); // Center hitbox
    }
    
    @Override
    public void update(double deltaTime) {
        
        double currentX = getX();
        double currentY = getY();
        
        if(movingUp.get()) currentY -= 300 * deltaTime;
        if(movingDown.get()) currentY += 300 * deltaTime;
        if(movingLeft.get()) currentX -= 300 * deltaTime;
        if(movingRight.get()) currentX += 300 * deltaTime;
        
        setPosition(currentX, currentY);
    }
    
    public void moveUp(boolean moving) { movingUp.set(moving); }
    public void moveDown(boolean moving) { movingDown.set(moving); }
    public void moveLeft(boolean moving) { movingLeft.set(moving); }
    public void moveRight(boolean moving) { movingRight.set(moving); }
    
    @Override
    public void render(GraphicsContext gc) {
        double renderSize = getScaledSize();
        gc.drawImage(texture, 
            x - renderSize/2, 
            y - renderSize/2, 
            renderSize, 
            renderSize
        );
    }
}