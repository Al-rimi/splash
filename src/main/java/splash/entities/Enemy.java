package splash.entities;

import javafx.scene.canvas.GraphicsContext;
import splash.utils.Vector2D;
import javafx.scene.image.Image;

public class Enemy extends Fish {
    private final Player player;
    private final Image leftImage;
    private final Image rightImage;
    private static final double SPEED = 150;
    private static final double CHASE_RADIUS = 300;
    private Vector2D randomDirection = new Vector2D();
    private double timeSinceLastDirectionChange = 0;
    private static final double DIRECTION_CHANGE_INTERVAL = 2.0;

    public Enemy(Player player, double x, double y, Image leftImage, Image rightImage) {
        this.player = player;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.size = player.getSize() * 2;
        setPosition(x, y);
        addTag("enemy");
        setHitboxOffset(-size/2, -size/2);
    }

    @Override
    public void update(double deltaTime) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.sqrt(dx*dx + dy*dy);

        if (distance <= CHASE_RADIUS) {
            // Chase player
            double length = Math.sqrt(dx*dx + dy*dy);
            if (length > 0) {
                dx /= length;
                dy /= length;
            }
            facingLeft = dx < 0;
            x += dx * SPEED * deltaTime;
            y += dy * SPEED * deltaTime;
        } else {
            // Random movement
            timeSinceLastDirectionChange += deltaTime;
            if (timeSinceLastDirectionChange >= DIRECTION_CHANGE_INTERVAL) {
                randomDirection.x = Math.random() * 2 - 1;
                randomDirection.y = Math.random() * 2 - 1;
                double length = Math.sqrt(randomDirection.x*randomDirection.x + randomDirection.y*randomDirection.y);
                if (length > 0) {
                    randomDirection.x /= length;
                    randomDirection.y /= length;
                }
                timeSinceLastDirectionChange = 0;
            }
            facingLeft = randomDirection.x < 0;
            x += randomDirection.x * SPEED * deltaTime;
            y += randomDirection.y * SPEED * deltaTime;
        }
        setPosition(x, y);
    }

    @Override
    public void render(GraphicsContext gc) {
        double renderSize = getScaledSize();
        Image image = facingLeft ? leftImage : rightImage;
        gc.drawImage(image, x - renderSize/2, y - renderSize/2, renderSize, renderSize);
    }
}