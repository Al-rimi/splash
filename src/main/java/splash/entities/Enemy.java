package splash.entities;

import javafx.scene.image.Image;

public class Enemy extends Fish {
    private static final double SPEED = 200;
    private static final double CHASE_RADIUS = 400;
    private static final double DIRECTION_CHANGE_INTERVAL = 2.0;
    private final Player player;

    public Enemy(Player player, double x, double y, Image leftTexture, Image rightTexture) {
        super(player.getSize() * 2);
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
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= CHASE_RADIUS) {
            pursue(player.getX(), player.getY(), SPEED, 0.1);
        } else {
            wander(deltaTime, DIRECTION_CHANGE_INTERVAL, SPEED, 0.05);
        }

        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }
}