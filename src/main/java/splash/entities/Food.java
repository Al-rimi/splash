package splash.entities;

import javafx.scene.canvas.GraphicsContext;
import splash.utils.Vector2D;
import javafx.scene.image.Image;

public class Food extends Fish {
    private final Player player;
    private final Image leftImage;
    private final Image rightImage;
    private static final double FLEE_RADIUS = 200;
    private static final double FLEE_SPEED = 100;
    private final int value;

    public Food(Player player, double x, double y, Image leftImage, Image rightImage) {
        this.player = player;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.size = player.getSize() * 0.5;
        this.value = (int) (Math.random() * 10) + 5;
        setPosition(x, y);
        addTag("food");
        setHitboxOffset(-size/2, -size/2);
    }

    @Override
    public void update(double deltaTime) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.sqrt(dx*dx + dy*dy);

        if (distance <= FLEE_RADIUS) {
            Vector2D fleeDirection = new Vector2D(x - player.getX(), y - player.getY());
            double length = Math.sqrt(fleeDirection.x*fleeDirection.x + fleeDirection.y*fleeDirection.y);
            if (length > 0) {
                fleeDirection.x /= length;
                fleeDirection.y /= length;
            }
            facingLeft = fleeDirection.x < 0;
            x += fleeDirection.x * FLEE_SPEED * deltaTime;
            y += fleeDirection.y * FLEE_SPEED * deltaTime;
            setPosition(x, y);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double renderSize = getScaledSize();
        Image image = facingLeft ? leftImage : rightImage;
        gc.drawImage(image, x - renderSize/2, y - renderSize/2, renderSize, renderSize);
    }

    public int getValue() { return value; }
}