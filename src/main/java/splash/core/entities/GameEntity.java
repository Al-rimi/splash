package splash.core.entities;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameEntity {
    protected double size;
    protected double x;
    protected double y;
    protected double scale = 1.0;
    
    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);
    
    public void updateScale(double scale) {
        this.scale = scale;
    }
    
    public double getSize() { return size; }
    public double getX() { return x; }
    public double getY() { return y; }
}