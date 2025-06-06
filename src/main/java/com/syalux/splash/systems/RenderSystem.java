package com.syalux.splash.systems;

import com.syalux.splash.data.Resource;
import com.syalux.splash.data.World;
import com.syalux.splash.entities.PlayerEntity;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RenderSystem {
    private final GraphicsContext gc;
    private final Canvas canvas;
    private final World world;
    private final double baseWidth;
    private final double baseHeight;

    public RenderSystem(GraphicsContext gc, Canvas canvas, World world, double baseWidth, double baseHeight) {
        this.gc = gc;
        this.canvas = canvas;
        this.world = world;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }

    /**
     * Renders a single frame of the game world. This method clears the canvas,
     * applies camera transformations (scaling and translation), draws water tiles,
     * and renders all entities (static, coins, NPCs, and players) within the view.
     * Finally, it applies a depth effect.
     *
     * @param camX The camera's X-coordinate in world space.
     * @param camY The camera's Y-coordinate in world space.
     * @param scaleX The scaling factor for the X-axis.
     * @param scaleY The scaling factor for the Y-axis.
     * @param depthEffectAlpha The alpha value for the depth effect, influencing water darkness.
     * @param player The player entity (currently unused in this method, but passed for consistency).
     */
    public void renderFrame(double camX, double camY, double scaleX, double scaleY,
                            double depthEffectAlpha, PlayerEntity player) {
        gc.save();
        gc.scale(Math.min(scaleX, scaleY), Math.min(scaleX, scaleY));

        double translateX = -camX + baseWidth / 2;
        double translateY = -camY + baseHeight / 2;
        double offsetX = (canvas.getWidth() / Math.min(scaleX, scaleY) - baseWidth) / 2;
        double offsetY = (canvas.getHeight() / Math.min(scaleX, scaleY) - baseHeight) / 2;

        world.getStaticEntities().forEach(e -> e.render(gc, camX, camY, baseWidth, baseHeight, offsetX, offsetY));
        world.getCoins().forEach(e -> e.render(gc, camX, camY, baseWidth, baseHeight, offsetX, offsetY));
        gc.translate(translateX + offsetX, translateY + offsetY);

        drawWaterTiles(camX, camY);
        world.getNpcs().forEach(e -> e.render(gc));
        world.getPlayers().forEach(e -> e.render(gc));

        gc.restore();
        gc.setFill(Color.rgb(0, 0, 0, depthEffectAlpha));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawWaterTiles(double camX, double camY) {
        final double tileSize = 1024;
        final double renderRadius = 1600;

        int minTileX = (int) Math.floor((camX - renderRadius) / tileSize);
        int maxTileX = (int) Math.ceil((camX + renderRadius) / tileSize);
        int minTileY = (int) Math.floor((camY - renderRadius) / tileSize);
        int maxTileY = (int) Math.ceil((camY + renderRadius) / tileSize);

        for (int xTile = minTileX; xTile <= maxTileX; xTile++) {
            for (int yTile = minTileY; yTile <= maxTileY; yTile++) {
                double tileWorldX = xTile * tileSize;
                double tileWorldY = yTile * tileSize;

                double closestX = Math.max(tileWorldX, Math.min(camX, tileWorldX + tileSize));
                double closestY = Math.max(tileWorldY, Math.min(camY, tileWorldY + tileSize));
                double dx = camX - closestX;
                double dy = camY - closestY;
                double distanceSq = dx * dx + dy * dy;

                if (distanceSq <= renderRadius * renderRadius) {
                    gc.drawImage(Resource.getWaterTexture(), tileWorldX, tileWorldY);
                }
            }
        }
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}