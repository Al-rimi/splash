package splash.core.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import splash.core.entities.GameEntity;
import splash.core.entities.Player;

public class RenderSystem {
    private final Canvas canvas;
    private final GraphicsContext gc;

    public RenderSystem(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void render(GameEntity entity) {
        if(entity instanceof Player) {
            Player player = (Player) entity;
            player.render(gc);
        }
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}