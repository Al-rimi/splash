package splash.engine;

import javafx.animation.AnimationTimer;

public abstract class GameLoop extends AnimationTimer {
    protected static final double NANOS_PER_SECOND = 1000000000.0;
    protected double lastUpdate = 0;
    
    @Override
    public void handle(long now) {
        double deltaTime = (now - lastUpdate) / NANOS_PER_SECOND;
        lastUpdate = now;
        update(deltaTime);
    }
    
    protected abstract void update(double deltaTime);
}
