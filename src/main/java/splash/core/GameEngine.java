package splash.core;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import splash.entities.*;

import java.util.*;

public class GameEngine extends AnimationTimer {
    private final double baseWidth;
    private final double baseHeight;
    private final Canvas canvas;
    private final GraphicsContext gc;

    private final World world;
    private final Player player;

    private double scaleX;
    private double scaleY;
    private double camX;
    private double camY;
    private double depthEffectAlpha = 0;
    private long lastUpdate = 0;

    private StackPane rootContainer;

    private final Map<String, List<Fish>> collisionLayers = new HashMap<>();
    private final Set<String> playerCollisionLayers = Set.of("enemy", "block", "food");

    public GameEngine(Player player, World world, Canvas canvas) {
        this.player = player;
        this.world = world;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.baseWidth = Config.GAME_WIDTH;
        this.baseHeight = Config.GAME_HEIGHT;
        this.camX = baseWidth;
        this.camY = baseHeight;
        this.scaleX = baseWidth;
        this.scaleY = baseHeight;

        initializeCollisionLayers();
        bindCanvasResize();
    }

    private void initializeCollisionLayers() {
        collisionLayers.put("enemy", new ArrayList<>());
        collisionLayers.put("food", new ArrayList<>());
        collisionLayers.put("block", new ArrayList<>());
    }

    private void bindCanvasResize() {
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
        updateDepthEffect();
        checkCollisions();
        renderFrame();
    }

    private void checkCollisions() {
        collisionLayers.values().forEach(List::clear);

        for (Fish entity : world.getEntities()) {
            for (String tag : entity.getTags()) {
                collisionLayers.getOrDefault(tag, new ArrayList<>()).add(entity);
            }
        }

        for (String layer : playerCollisionLayers) {
            List<Fish> entities = collisionLayers.get(layer);
            if (entities == null) continue;

            for (Fish entity : entities) {
                if (!player.getBounds().intersects(entity.getBounds())) continue;

                if (entity instanceof Enemy) {
                    player.healthProperty().set(player.healthProperty().get() - 1);
                } else if (entity instanceof Food food) {
                    player.pointsProperty().set(player.pointsProperty().get() + food.getValue());
                    world.removeEntity(entity);
                }
            }
        }
    }

    private void renderFrame() {
        clear();
        gc.save();

        double scale = Math.min(scaleX, scaleY);
        gc.scale(scale, scale);

        double translateX = -camX + baseWidth / 2;
        double translateY = -camY + baseHeight / 2;
        double offsetX = (canvas.getWidth() / scale - baseWidth) / 2;
        double offsetY = (canvas.getHeight() / scale - baseHeight) / 2;

        gc.translate(translateX + offsetX, translateY + offsetY);

        world.getEntities().forEach(this::renderEntity);
        renderEntity(player);

        gc.restore();

        gc.setFill(Color.rgb(0, 0, 0, depthEffectAlpha));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void renderEntity(Fish entity) {
        if (entity instanceof Player p) p.render(gc);
        else if (entity instanceof Enemy e) e.render(gc);
        else if (entity instanceof Food f) f.render(gc);
    }

    private void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void updateCameraPosition(double deltaTime) {
        camX += (player.getX() - camX) * deltaTime;
        camY += (player.getY() - camY) * deltaTime * 2;
    }

    private void updateDepthEffect() {
        double depth = (camY * 2 - baseHeight) / Config.DEPTH_DIVISOR;
        depthEffectAlpha = depth <= 0 ? 0 : (Math.min(depth, 1.0) * Config.MAX_DEPTH_ALPHA);
    }

    public void setupInputHandling() {
        if (rootContainer == null) return;

        rootContainer.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W, UP -> player.moveUp(true);
                case S, DOWN -> player.moveDown(true);
                case A, LEFT -> player.moveLeft(true);
                case D, RIGHT -> player.moveRight(true);
                default -> {}
            }
        });

        rootContainer.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W, UP -> player.moveUp(false);
                case S, DOWN -> player.moveDown(false);
                case A, LEFT -> player.moveLeft(false);
                case D, RIGHT -> player.moveRight(false);
                default -> {}
            }
        });
    }

    private void updateScale() {
        scaleX = canvas.getWidth() / baseWidth;
        scaleY = canvas.getHeight() / baseHeight;

        player.updateScale(scaleY);
        world.updateWorldScale(player.getScaledSize());
    }

    public void setScale(double scaleX, double scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
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