package splash.entities;

import javafx.scene.image.Image;
import splash.core.Config;

public class Food extends Fish {
    private static final double DIRECTION_CHANGE_INTERVAL = 3.0;

    private final Player player;
    private final int value;

    public Food(Player player, double x, double y, Image leftTexture, Image rightTexture) {
        super(player.getSize() * Config.FOOD_SIZE_MULTIPLIER);
        this.player = player;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        this.value = (int) (Math.random() * 10) + 5;
        setPosition(x, y);
        addTag("food");
    }

    @Override
    public void update(double deltaTime) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.hypot(dx, dy);

        if (distance <= Config.FOOD_DETECTION_RADIUS) {
            fleeFrom(player.getX(), player.getY(), Config.FOOD_SPEED, 0.1);
        } else {
            wander(deltaTime, DIRECTION_CHANGE_INTERVAL, Config.FOOD_SPEED * 0.6, 0.05);
        }

        updatePosition(deltaTime);
    }

    private void updatePosition(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }

    public int getValue() { return value; }
}
