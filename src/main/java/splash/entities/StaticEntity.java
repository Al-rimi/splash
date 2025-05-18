package splash.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class StaticEntity {
    private final double x;
    private final double y;
    private final Image image;
    private final double scale;
    private final double parallaxFactor;

    public StaticEntity(double x, double y, Image image, double scale, double parallaxFactor) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.scale = scale;
        this.parallaxFactor = parallaxFactor;
    }

    public void render(GraphicsContext gc, double camX, double camY, double baseWidth, double baseHeight, double offsetX, double offsetY) {
        double screenX = (x - camX * parallaxFactor) + baseWidth / 2 + offsetX;
        double screenY = (y - camY * parallaxFactor) + baseHeight / 2 + offsetY;
        double width = image.getWidth() * scale;
        double height = image.getHeight() * scale;
        gc.drawImage(image, screenX - width/2, screenY - height/2, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}