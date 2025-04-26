package splash.core.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Food extends GameEntity {
    private final int value;
    
    public Food(double x, double y, int value) {
        this.size = 30;
        setPosition(x, y);
        addTag("food");
        setHitboxOffset(-size/2, -size/2);        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillRect(x - size/2, y - size/2, size, size);
    }
    
    public int getValue() { return value; }
}
