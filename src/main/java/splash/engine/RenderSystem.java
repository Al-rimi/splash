package splash.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import splash.entities.Enemy;
import splash.entities.Fish;
import splash.entities.Food;
import splash.entities.Player;

public class RenderSystem {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private double scaleX = 1.0;
    private double scaleY = 1.0;

    public RenderSystem(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setScale(double scaleX, double scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void render(Fish entity) {
        // Apply inverse scaling to maintain entity proportions
        gc.save();
        gc.scale(1/scaleX, 1/scaleY);
        
        if(entity instanceof Player) {
            ((Player) entity).render(gc);
        }
        else if(entity instanceof Enemy) {
            ((Enemy) entity).render(gc);
        }
        else if(entity instanceof Food) {
            ((Food) entity).render(gc);
        }
        
        gc.restore();
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }
}