package splash.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import splash.entities.*;
import splash.utils.GameManager;

import java.util.*;

public class GameEngine extends AnimationTimer {
    private final double baseWidth;
    private final double baseHeight;
    private final Canvas canvas;
    private final World world;
    private final Player player;
    private final GraphicsContext gc;
    private final Map<String, List<Fish>> collisionLayers = new HashMap<>();
    private final Set<String> playerCollisionLayers = Set.of("enemy", "block", "food");
    private long lastUpdate = 0;
    private double scaleX;
    private double scaleY;
    private double camX;
    private double camY;
    private StackPane rootContainer;

    public GameEngine(Player player, World world, Canvas canvas) {
        this.player = player;
        this.world = world;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.baseWidth = GameManager.getGameWidth();
        this.baseHeight = GameManager.getGameHeight();
        this.camX = baseWidth / 2;
        this.camY = baseHeight / 2;
        this.scaleX = canvas.getWidth() / baseWidth;
        this.scaleY = canvas.getHeight() / baseHeight;

        this.collisionLayers.put("enemy", new ArrayList<>());
        this.collisionLayers.put("food", new ArrayList<>());
        this.collisionLayers.put("block", new ArrayList<>());

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateScale());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateScale());
    }

    @Override
    public void handle(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }
        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
        lastUpdate = now;
        update(deltaTime);
    }

    private void update(double deltaTime) {
        player.update(deltaTime);
        setupInputHandling();
        world.getEntities().forEach(e -> e.update(deltaTime));
        updateCameraPosition(deltaTime);
        checkCollisions();
        renderFrame();
    }

    private void checkCollisions() {
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

        for (String layer : playerCollisionLayers) {
            List<Fish> entities = collisionLayers.get(layer);
            if (entities == null) continue;
            for (Fish entity : entities) {
                if (player.getBounds().intersects(entity.getBounds())) {
                    if (entity instanceof Enemy) {
                        player.healthProperty().set(player.healthProperty().get() - 1);
                    } else if (entity instanceof Food) {
                        player.pointsProperty().set(player.pointsProperty().get() + ((Food) entity).getValue());
                        world.removeEntity(entity);
                    }
                }
            }
        }
    }

    private void renderFrame() {
        clear();
        gc.save();

        double scale = Math.min(scaleX, scaleY);
        gc.scale(scale, scale);

        double offsetX = (baseWidth - (baseWidth * (scaleX / scale))) / 2;
        double offsetY = (baseHeight - (baseHeight * (scaleY / scale))) / 2;

        double cameraTranslateX = -camX + baseWidth / 2;
        double cameraTranslateY = -camY + baseHeight / 2;
        gc.translate(offsetX + cameraTranslateX, offsetY + cameraTranslateY);

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

    public void setupInputHandling() {
        rootContainer.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                case UP:
                    player.moveUp(true);
                    break;
                case S:
                case DOWN:
                    player.moveDown(true);
                    break;
                case A:
                case LEFT:
                    player.moveLeft(true);
                    break;
                case D:
                case RIGHT:
                    player.moveRight(true);
                    break;
                default:
                    break;
            }
        });
        rootContainer.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W:
                case UP:
                    player.moveUp(false);
                    break;
                case S:
                case DOWN:
                    player.moveDown(false);
                    break;
                case A:
                case LEFT:
                    player.moveLeft(false);
                    break;
                case D:
                case RIGHT:
                    player.moveRight(false);
                    break;
                default:
                    break;
            }
        });
    }

    private void updateCameraPosition(double deltaTime) {
        this.camX += (player.getX() - camX) * deltaTime;
        this.camY += (player.getY() - camY) * deltaTime * 2;
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
    
    public StackPane getRootContainer() {
        return rootContainer;
    }

    public void setRootContainer(StackPane rootContainer) {
        this.rootContainer = rootContainer;
    }

    public double getBaseWidth() {
        return baseWidth;
    }

    public double getBaseHeight() {
        return baseHeight;
    }
}