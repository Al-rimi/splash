package com.syalux.splash.systems;

import com.syalux.splash.core.ResourceManager;
import com.syalux.splash.entities.*;

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

    public void renderFrame(double camX, double camY, double scaleX, double scaleY, 
                           double depthEffectAlpha, Player player) {
        gc.save();
        gc.scale(Math.min(scaleX, scaleY), Math.min(scaleX, scaleY));

        double translateX = -camX + baseWidth / 2;
        double translateY = -camY + baseHeight / 2;
        double offsetX = (canvas.getWidth() / Math.min(scaleX, scaleY) - baseWidth) / 2;
        double offsetY = (canvas.getHeight() / Math.min(scaleX, scaleY) - baseHeight) / 2;

        world.getStaticEntities().forEach(e -> e.render(gc, camX, camY, baseWidth, baseHeight, offsetX, offsetY));
        gc.translate(translateX + offsetX, translateY + offsetY);

        drawWaterTiles(player);
        world.getNpcs().forEach(e -> e.render(gc));
        world.getPlayers().forEach(e -> e.render(gc));

        gc.restore();
        gc.setFill(Color.rgb(0, 0, 0, depthEffectAlpha));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawWaterTiles(Player player) {

        final double tileSize = 1024;
        final double renderRadius = 1600;

        double playerX = player.getX();
        double playerY = player.getY();

        int minTileX = (int) Math.floor((playerX - renderRadius) / tileSize);
        int maxTileX = (int) Math.ceil((playerX + renderRadius) / tileSize);
        int minTileY = (int) Math.floor((playerY - renderRadius) / tileSize);
        int maxTileY = (int) Math.ceil((playerY + renderRadius) / tileSize);

        for (int xTile = minTileX; xTile <= maxTileX; xTile++) {
            for (int yTile = minTileY; yTile <= maxTileY; yTile++) {
                double tileWorldX = xTile * tileSize;
                double tileWorldY = yTile * tileSize;

                double closestX = Math.max(tileWorldX, Math.min(playerX, tileWorldX + tileSize));
                double closestY = Math.max(tileWorldY, Math.min(playerY, tileWorldY + tileSize));
                double dx = playerX - closestX;
                double dy = playerY - closestY;
                double distanceSq = dx * dx + dy * dy;

                if (distanceSq <= renderRadius * renderRadius) {
                    gc.drawImage(ResourceManager.getWaterTexture(), tileWorldX, tileWorldY);
                }
            }
        }
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}