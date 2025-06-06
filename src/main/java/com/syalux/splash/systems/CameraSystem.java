package com.syalux.splash.systems;

import com.syalux.splash.data.Config;
import com.syalux.splash.entities.FishEntity;
import com.syalux.splash.entities.PlayerEntity;

public class CameraSystem {
    private FishEntity targetOverride;
    private double camX;
    private double camY;
    private final double baseHeight;
    private double depthEffectAlpha = 0;

    public CameraSystem(double baseWidth, double baseHeight) {
        this.baseHeight = baseHeight;
        this.camX = baseWidth / 2;
        this.camY = baseHeight / 2;
    }

    public void setTargetOverride(FishEntity target) {
        this.targetOverride = target;
    }

    /**
     * Updates the camera's position to follow the target (player or overridden target)
     * and calculates the depth effect alpha based on the camera's vertical position.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     * @param player The player entity, used as the default target if no override is set.
     */
    public void update(double deltaTime, PlayerEntity player) {
        FishEntity target = (targetOverride != null && !targetOverride.isDead()) ? targetOverride : player;
        camX += (target.getX() - camX) * deltaTime;
        camY += (target.getY() - camY) * deltaTime * 2;
        updateDepthEffect();
    }

    private void updateDepthEffect() {
        double depth = (camY * 2 - baseHeight) / Config.DEPTH_DIVISOR;
        depthEffectAlpha = depth <= 0 ? 0 : (Math.min(depth, 1.0) * Config.MAX_DEPTH_ALPHA);
    }

    public double getCamX() { return camX; }
    public double getCamY() { return camY; }
    public double getDepthEffectAlpha() { return depthEffectAlpha; }

    public double getViewCenterX() {
        return camX;
    }

    public double getViewCenterY() {
        return camY;
    }
}