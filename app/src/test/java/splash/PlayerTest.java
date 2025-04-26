package splash;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import splash.core.entities.Player;
import javafx.scene.image.Image;

public class PlayerTest {
    @Test
    void testPlayerMovement() {
        // Use a placeholder image
        Image dummyImage = new Image(getClass().getResourceAsStream("/images/character.png"));
        Player player = new Player(dummyImage);
        
        double initialY = player.getY();
        player.moveUp(true);
        player.update(0.016);
        assertTrue(player.getY() < initialY, "Player should move up");
    }
}