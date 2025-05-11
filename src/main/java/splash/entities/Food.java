package splash.entities;

import javafx.scene.image.Image;

public class Food extends Fish {
    private static final double FLEE_RADIUS = 400;
    private static final double FLEE_SPEED = 200;
    private static final double DIRECTION_CHANGE_INTERVAL = 3.0;
    private final Player player;
    private final int value;

    public Food(Player player, double x, double y, Image leftTexture, Image rightTexture) {
        super(player.getSize() * 0.5);
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
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= FLEE_RADIUS) {
            fleeFrom(player.getX(), player.getY(), FLEE_SPEED, 0.1);
        } else {
            wander(deltaTime, DIRECTION_CHANGE_INTERVAL, FLEE_SPEED * 0.6, 0.05);
        }

        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        setPosition(x, y);
    }

    public int getValue() { return value; }
}