package splash.core;

public class Config {
    // Window Configuration
    public static final int GAME_WIDTH = 1920;
    public static final int GAME_HEIGHT = 1080;
    public static final String GAME_TITLE = "Splash";
    
    // Player Configuration
    public static final double PLAYER_BASE_SIZE = 50;
    public static final double PLAYER_MAX_SPEED = 300;
    
    // Camera Configuration
    public static final double DEPTH_DIVISOR = 10000.0;
    public static final double MAX_DEPTH_ALPHA = 0.95;
    public static final double CAMERA_LERP_SPEED = 2.0;
    
    // Enemy Configuration
    public static final double ENEMY_BASE_SIZE_MULTIPLIER = 2.0;
    public static final double ENEMY_SPEED = 200;
    public static final double ENEMY_DETECTION_RADIUS = 500;
    
    // Food Configuration
    public static final double FOOD_SIZE_MULTIPLIER = 0.5;
    public static final double FOOD_SPEED = 200;
    public static final double FOOD_DETECTION_RADIUS = 500;
    
    // World Configuration
    public static final double MIN_WORLD_SCALE = 1.0;
    public static final double WORLD_SCALE_DIVIDER = 10.0;
    public static final double SPAWN_RADIUS = 1000;
    public static final double DESPAWN_RADIUS = 4000;

    // Resource Configuration
    public static final String DEFAULT_LANGUAGE = "ar";
    public static final int BACKGROUND_IMAGE_COUNT = 4;
    public static final int FISH_IMAGE_COUNT = 19;
}