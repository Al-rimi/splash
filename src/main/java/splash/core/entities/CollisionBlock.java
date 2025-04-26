package splash.core.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CollisionBlock extends GameEntity {
    public CollisionBlock(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
        setPosition(x, y);
        addTag("block");
        setHitboxOffset(-this.size/2, -this.size/2);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(x - size/2, y - size/2, size, size);
    }
}
