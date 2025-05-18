package splash.entities;

import java.util.concurrent.ConcurrentLinkedQueue;
import splash.core.Config;

public class World {
    private final ConcurrentLinkedQueue<Fish> entities = new ConcurrentLinkedQueue<>();
    private double worldScale = Config.MIN_WORLD_SCALE;

    public void updateWorldScale(double playerSize) {
        worldScale = Math.max(Config.MIN_WORLD_SCALE, playerSize / Config.WORLD_SCALE_DIVIDER);
        updateEntityScales();
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

    private void updateEntityScales() {
        for (Fish entity : entities) {
            entity.updateScale(worldScale);
        }
    }
}