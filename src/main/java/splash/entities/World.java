package splash.entities;

import java.util.concurrent.ConcurrentLinkedQueue;

public class World {
    private static final double MIN_WORLD_SCALE = 1.0;
    private static final double SCALE_DIVIDER = 10.0;

    private final ConcurrentLinkedQueue<Fish> entities = new ConcurrentLinkedQueue<>();
    private double worldScale = MIN_WORLD_SCALE;

    public void updateWorldScale(double playerSize) {
        worldScale = Math.max(MIN_WORLD_SCALE, playerSize / SCALE_DIVIDER);
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