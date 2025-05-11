package splash.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import splash.entities.*;
import java.util.*;

public class GameEngine extends AnimationTimer {
    private static final double NANOS_PER_SECOND = 1_000_000_000.0;
    private final Player player;
    private final World world;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Map<String, List<Fish>> collisionLayers = new HashMap<>();
    private final Set<String> playerCollisionLayers = Set.of("enemy", "block", "food");
    private long lastUpdate = 0;
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    private final double baseWidth;
    private final double baseHeight;
    private double camX;
    private double camY;
    private static final double DEAD_ZONE_WIDTH = 400;
    private static final double DEAD_ZONE_HEIGHT = 300;
    private static final double CAMERA_LERP_FACTOR = 0.1;

    public GameEngine(Player player, World world, Canvas canvas) {
        this.player = player;
        this.world = world;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.baseWidth = 1280;
        this.baseHeight = 720;
        this.camX = baseWidth / 2;
        this.camY = baseHeight / 2;
        initCollisionLayers();

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateScale());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateScale());
    }

    private void initCollisionLayers() {
        collisionLayers.put("enemy", new ArrayList<>());
        collisionLayers.put("food", new ArrayList<>());
        collisionLayers.put("block", new ArrayList<>());
    }

    @Override
    public void handle(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }
        double deltaTime = (now - lastUpdate) / NANOS_PER_SECOND;
        lastUpdate = now;
        update(deltaTime);
    }

    private void update(double deltaTime) {
        player.update(deltaTime);
        world.getEntities().forEach(e -> e.update(deltaTime));
        updateCameraPosition(deltaTime);
        checkCollisions();
        renderFrame();
    }

    private void checkCollisions() {
        updateCollisionLayers();
        checkPlayerCollisions();
    }

    private void updateCollisionLayers() {
        for (List<Fish> layer : collisionLayers.values()) {
            layer.clear();
        }
        for (Fish entity : world.getEntities()) {
            for (String tag : entity.getTags()) {
                List<Fish> layer = collisionLayers.get(tag);
                if (layer != null) {
                    layer.add(entity);
                }
            }
        }
    }

    private void checkPlayerCollisions() {
        for (String layer : playerCollisionLayers) {
            List<Fish> entities = collisionLayers.get(layer);
            if (entities == null)
                continue;
            for (Fish entity : entities) {
                if (checkCollision(player, entity)) {
                    handleCollision(player, entity);
                }
            }
        }
    }

    private boolean checkCollision(Fish a, Fish b) {
        return a.getBounds().intersects(b.getBounds());
    }

    private void handleCollision(Player player, Fish entity) {
        if (entity instanceof Enemy) {
            handlePlayerEnemyCollision(player, (Enemy) entity);
        } else if (entity instanceof Food) {
            handlePlayerFoodCollision(player, (Food) entity);
        }
    }

    private void handlePlayerEnemyCollision(Player player, Enemy enemy) {
        player.healthProperty().set(player.healthProperty().get() - 1);
    }

    private void handlePlayerFoodCollision(Player player, Food food) {
        player.pointsProperty().set(player.pointsProperty().get() + food.getValue());
        world.removeEntity(food);
    }

    private void renderFrame() {
        clear();
        gc.save();
        
        double scale = Math.min(scaleX, scaleY);
        gc.scale(scale, scale);

        // Calculate offsets for letterboxing
        double offsetX = (baseWidth - (baseWidth * (scaleX / scale))) / 2;
        double offsetY = (baseHeight - (baseHeight * (scaleY / scale))) / 2;

        // Apply camera translation
        double cameraTranslateX = -camX + baseWidth / 2;
        double cameraTranslateY = -camY + baseHeight / 2;
        gc.translate(offsetX + cameraTranslateX, offsetY + cameraTranslateY);

        // Render entities
        world.getEntities().forEach(this::renderEntity);
        renderEntity(player);
        
        gc.restore();
    }

    private void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void renderEntity(Fish entity) {
        if (entity instanceof Player) {
            ((Player) entity).render(gc);
        } else if (entity instanceof Enemy) {
            ((Enemy) entity).render(gc);
        } else if (entity instanceof Food) {
            ((Food) entity).render(gc);
        }
    }

    private void updateCameraPosition(double deltaTime) {
        double playerX = player.getX();
        double playerY = player.getY();

        // Keep camera centered on player at start
        if(camX == baseWidth/2 && camY == baseHeight/2) {
            camX = playerX;
            camY = playerY;
        }

        // Calculate desired camera position based on dead zone
        double desiredCamX = camX;
        if (playerX < camX - DEAD_ZONE_WIDTH / 2) {
            desiredCamX = playerX + DEAD_ZONE_WIDTH / 2;
        } else if (playerX > camX + DEAD_ZONE_WIDTH / 2) {
            desiredCamX = playerX - DEAD_ZONE_WIDTH / 2;
        }

        double desiredCamY = camY;
        if (playerY < camY - DEAD_ZONE_HEIGHT / 2) {
            desiredCamY = playerY + DEAD_ZONE_HEIGHT / 2;
        } else if (playerY > camY + DEAD_ZONE_HEIGHT / 2) {
            desiredCamY = playerY - DEAD_ZONE_HEIGHT / 2;
        }

        // Smoothly interpolate camera position
        camX += (desiredCamX - camX) * CAMERA_LERP_FACTOR;
        camY += (desiredCamY - camY) * CAMERA_LERP_FACTOR;
    }

    public void setScale(double scaleX, double scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    private void updateScale() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        scaleX = width / baseWidth;
        scaleY = height / baseHeight;
        player.updateScale(scaleX);
        world.updateWorldScale(player.getScaledSize());
    }

    public double getBaseWidth() {
        return baseWidth;
    }

    public double getBaseHeight() {
        return baseHeight;
    }
}