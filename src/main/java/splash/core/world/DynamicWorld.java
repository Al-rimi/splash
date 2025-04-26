package splash.core.world;

import splash.core.entities.GameEntity;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DynamicWorld {
    private final ConcurrentLinkedQueue<GameEntity> entities = new ConcurrentLinkedQueue<>();
    private double worldScale = 1.0;
    
    public void updateWorldScale(double playerSize) {
        worldScale = Math.max(1.0, playerSize / 10.0);
        entities.forEach(entity -> entity.updateScale(worldScale));
    }
    
    public void spawnEntity(GameEntity entity) {
        entities.add(entity);
    }
}