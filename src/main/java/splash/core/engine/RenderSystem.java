package splash.core.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import splash.core.entities.Enemy;
import splash.core.entities.Food;
import splash.core.entities.Fish;
import splash.core.entities.Player;

public class RenderSystem {
    private final Canvas canvas;
    private final GraphicsContext gc;

    public RenderSystem(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void render(Fish entity) {
        if(entity instanceof Player) {
            ((Player) entity).render(gc);
        }
        else if(entity instanceof Enemy) {
            ((Enemy) entity).render(gc);
        }
        else if(entity instanceof Food) {
            ((Food) entity).render(gc);
        }
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}