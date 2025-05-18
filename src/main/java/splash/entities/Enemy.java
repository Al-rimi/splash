package splash.entities;

import javafx.scene.image.Image;
import splash.core.Config;
public class Enemy extends Fish {
    private static final double DIRECTION_CHANGE_INTERVAL = 2.0;

    private final Player player;

    public Enemy(Player player, double x, double y, Image leftTexture, Image rightTexture) {
        super(player.getSize() * Config.ENEMY_BASE_SIZE_MULTIPLIER);
        this.player = player;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        setPosition(x, y);
        addTag("enemy");
    }

    @Override
    public void update(double deltaTime) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.hypot(dx, dy);

        if (distance <= Config.ENEMY_DETECTION_RADIUS) {
            pursue(player.getX(), player.getY(), Config.ENEMY_SPEED, 0.1);
        } else {
            wander(deltaTime, DIRECTION_CHANGE_INTERVAL, Config.ENEMY_SPEED, 0.05);
        }

        updatePosition(deltaTime);
    }

    private void updatePosition(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }
}