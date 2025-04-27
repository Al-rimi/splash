package splash.core.world;

import splash.core.entities.Fish;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DynamicWorld {
    private final ConcurrentLinkedQueue<Fish> entities = new ConcurrentLinkedQueue<>();
    private double worldScale = 1.0;
    
    public void updateWorldScale(double playerSize) {
        worldScale = Math.max(1.0, playerSize / 10.0);
        entities.forEach(entity -> entity.updateScale(worldScale));
    }
    
    public void spawnEntity(Fish entity) {
        entities.add(entity);
    }

    public ConcurrentLinkedQueue<Fish> getEntities() {
        return entities;
    }
    
    public void removeEntity(Fish entity) {
        entities.remove(entity);
    }
}