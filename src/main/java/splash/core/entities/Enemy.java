package splash.core.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import splash.core.utils.Vector2D;

public class Enemy extends Fish {
    private static final double SPEED = 150;
    private final Player player;
    private final Vector2D direction = new Vector2D();

    public Enemy(Player player, double x, double y) {
        this.player = player;
        this.size = 40;
        setPosition(x, y);
        addTag("enemy");
        setHitboxOffset(-size/2, -size/2); // Center hitbox
    }

    @Override
    public void update(double deltaTime) {
        direction.x = player.getX() - x;
        direction.y = player.getY() - y;
        double length = Math.sqrt(direction.x*direction.x + direction.y*direction.y);
        
        if(length > 0) {
            direction.x /= length;
            direction.y /= length;
        }
        
        double newX = x + direction.x * SPEED * deltaTime;
        double newY = y + direction.y * SPEED * deltaTime;
        setPosition(newX, newY);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x - size/2, y - size/2, size, size);
    }
}