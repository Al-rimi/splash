package splash.core;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import splash.entities.*;
import splash.systems.CameraSystem;
import splash.systems.CollisionSystem;
import splash.systems.RenderSystem;
import splash.systems.SpawnSystem;

public class GameEngine extends AnimationTimer {
    private final double baseWidth;
    private final double baseHeight;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Image waterTexture;

    private final World world;
    private final Player player;

    private double scaleX;
    private double scaleY;
    private long lastUpdate = 0;
    private boolean isPaused = false;
    private final Runnable onPausePressed;
    private final CameraSystem cameraSystem;
    private final CollisionSystem collisionSystem;
    private final RenderSystem renderSystem;
    private final SpawnSystem spawnSystem;

    private StackPane rootContainer;

    public GameEngine(Player player, Canvas canvas, Runnable onPausePressed) {
        this.player = player;
        this.world = new World();
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.baseWidth = Config.GAME_WIDTH;
        this.baseHeight = Config.GAME_HEIGHT;
        this.scaleX = baseWidth;
        this.scaleY = baseHeight;
        this.waterTexture = ResourceManager.getWaterTexture();
        this.onPausePressed = onPausePressed;
        this.spawnSystem = new SpawnSystem(world);
        this.cameraSystem = new CameraSystem(baseWidth, baseHeight);
        this.collisionSystem = new CollisionSystem(world);
        this.renderSystem = new RenderSystem(gc, canvas, world, waterTexture, baseWidth, baseHeight);

        bindCanvasResize();
        spawnSystem.spawnPlayer(player);
        spawnSystem.start();
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

        if (isPaused) return;

        update(deltaTime);
    }

    private void update(double deltaTime) {
        world.getPlayers().forEach(p -> p.update(deltaTime));
        world.getNpcs().forEach(npc -> npc.update(deltaTime));

        cameraSystem.update(deltaTime, player);
        collisionSystem.checkCollisions();
        renderSystem.renderFrame(
            cameraSystem.getCamX(), 
            cameraSystem.getCamY(),
            scaleX, scaleY,
            cameraSystem.getDepthEffectAlpha(),
            player
        );
    }

    public void setupInputHandling() {
        if (rootContainer == null) return;

        rootContainer.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W, UP -> player.moveUp(true);
                case S, DOWN -> player.moveDown(true);
                case A, LEFT -> player.moveLeft(true);
                case D, RIGHT -> player.moveRight(true);
                case ESCAPE -> onPausePressed.run();
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
    }

    public void setRootContainer(StackPane rootContainer) {
        this.rootContainer = rootContainer;
        setupInputHandling();
    }

    public void pause(boolean paused) {
        this.isPaused = paused;
        if (paused) {
            spawnSystem.pause();
        } else {
            spawnSystem.start();
        }
    }
}