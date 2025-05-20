package splash.core;

public class Config {
    // Window Configuration
    public static final int GAME_WIDTH = 1920;
    public static final int GAME_HEIGHT = 1080;
    public static final String GAME_TITLE = "Splash";
    public static final double SPAWN_RADIUS = 10000;
    public static final double DESPAWN_RADIUS = 40000;
    public static final double SPAWN_DURATION_SECONDS = 0.1;
    public static final double SPAWN_FOOD_PROBABILITY = 0.9;
    public static final double SPAWN_ENEMY_PROBABILITY = 0.1;
    public static final double SPAWN_MOUNTAIN_PROBABILITY = 0.4;
    public static final double SPAWN_SEAWEED_PROBABILITY = 1.0;
    public static final double SPAWN_ROCK_PROBABILITY = 0.3;
    
    // Player Configuration
    public static final double PLAYER_BASE_SIZE = 60;
    public static final double PLAYER_MAX_SPEED = 500;
    
    // Camera Configuration
    public static final double DEPTH_DIVISOR = 10000.0;
    public static final double MAX_DEPTH_ALPHA = 0.95;

    // Resource Configuration
    public static final String DEFAULT_LANGUAGE = "en";
    public static final int FISH_IMAGE_COUNT = 19;
    public static final int MOUNTAIN_IMAGE_COUNT = 3;
    public static final int SEAWEED_IMAGE_COUNT = 2;
    public static final int ROCK_IMAGE_COUNT = 2;
}