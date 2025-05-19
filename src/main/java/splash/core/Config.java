package splash.core;

public class Config {
    // Window Configuration
    public static final int GAME_WIDTH = 1920;
    public static final int GAME_HEIGHT = 1080;
    public static final String GAME_TITLE = "Splash";
    public static final double SPAWN_DURATION_SECONDS = 0.5;
    public static final double SPAWN_FOOD_PROBABILITY = 0.9;
    public static final double SPAWN_ENEMY_PROBABILITY = 0.4;
    public static final double SPAWN_MUNTION_PROBABILITY = 0.2;
    public static final double SPAWN_SEAWEED_PROBABILITY = 1.0;
    public static final double SPAWN_ROCK_PROBABILITY = 0.3;
    
    // Player Configuration
    public static final double PLAYER_BASE_SIZE = 60;
    public static final double PLAYER_MAX_SPEED = 500;
    
    // Camera Configuration
    public static final double DEPTH_DIVISOR = 10000.0;
    public static final double MAX_DEPTH_ALPHA = 0.95;
    public static final double CAMERA_LERP_SPEED = 2.0;
    
    // Enemy configuration
    public static final double ENEMY_MAX_SIZE_MULTIPLIER = 4;
    public static final double ENEMY_MIN_SIZE_MULTIPLIER = 2;
    public static final double ENEMY_DETECTION_RADIUS = 700;
    public static final double ENEMY_MOVEMENT_SPEED = 300;
    public static final double ENEMY_DIRECTION_CHANGE_INTERVAL = 2.0;

    // Food configuration
    public static final double FOOD_MAX_SIZE_MULTIPLIER = 0.9;
    public static final double FOOD_MIN_SIZE_MULTIPLIER = 0.4;
    public static final double FOOD_DETECTION_RADIUS = 400;
    public static final double FOOD_MOVEMENT_SPEED = 200;
    public static final double FOOD_DIRECTION_CHANGE_INTERVAL = 3.0;
    
    // World Configuration
    public static final double MIN_WORLD_SCALE = 1.0;
    public static final double WORLD_SCALE_DIVIDER = 10.0;
    public static final double SPAWN_RADIUS = 1000;
    public static final double DESPAWN_RADIUS = 4000;

    // Resource Configuration
    public static final String DEFAULT_LANGUAGE = "en";
    public static final int FISH_IMAGE_COUNT = 19;
    public static final int MOUNTAIN_IMAGE_COUNT = 3;
    public static final int SEAWEED_IMAGE_COUNT = 2;
    public static final int ROCK_IMAGE_COUNT = 2;
}