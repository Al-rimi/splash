package splash.entities;

import java.util.concurrent.ConcurrentLinkedQueue;

public class World {
    private final ConcurrentLinkedQueue<Fish> entities = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<StaticEntity> staticEntities = new ConcurrentLinkedQueue<>();

    public void spawnEntity(Fish entity) {
        entities.add(entity);
    }

    public void spawnStaticEntity(StaticEntity entity) {
        staticEntities.add(entity);
    }

    public ConcurrentLinkedQueue<Fish> getEntities() {
        return entities;
    }

    public ConcurrentLinkedQueue<StaticEntity> getStaticEntities() {
        return staticEntities;
    }

    public void removeEntity(Fish entity) {
        entities.remove(entity);
    }

    public void removeStaticEntity(StaticEntity entity) {
        staticEntities.remove(entity);
    }
}