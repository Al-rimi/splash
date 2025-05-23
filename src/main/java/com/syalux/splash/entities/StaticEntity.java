package com.syalux.splash.entities;

import com.syalux.splash.core.ResourceManager;
import javafx.scene.canvas.GraphicsContext;

public class StaticEntity {
    private final ResourceManager.Environment type;
    private final double x;
    private final double y;
    private final int imageNumber;
    private final int size;
    private final double parallaxFactor;

    public StaticEntity(ResourceManager.Environment type, 
                        double x, 
                        double y, 
                        int imageNumber, 
                        int size) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.imageNumber = imageNumber;
        this.size = size;
        if (type == ResourceManager.Environment.MOUNTAIN) {
            this.parallaxFactor = 1000.0/size;
        } else {
            this.parallaxFactor = size / 200.0;
        }
    }

    public void render(GraphicsContext gc, 
                       double camX, 
                       double camY, 
                       double baseWidth, 
                       double baseHeight,
                       double offsetX, 
                       double offsetY) {

        double screenX = (x - camX * parallaxFactor) + baseWidth/2 + offsetX;
        double screenY = (y - camY * parallaxFactor) + baseHeight/2 + offsetY;
        
        gc.drawImage(ResourceManager.getEnvironmentImage(type, imageNumber), 
            screenX - size, 
            screenY - size, 
            size, 
            size
        );
    }

    public double getX() { return x; }
    public double getY() { return y; }
}