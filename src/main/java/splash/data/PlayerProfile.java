package splash.data;

public class PlayerProfile {
    private String playerName;
    private int highScore;
    private int coins;
    private int selectedCharacter;

    public PlayerProfile() {
        this.playerName = "Player";
        this.highScore = 0;
        this.coins = 0;
        this.selectedCharacter = 1;
    }
    
    public PlayerProfile(String playerName, int highScore, int coins, int selectedCharacter) {
        this.playerName = playerName;
        this.highScore = highScore;
        this.coins = coins;
        this.selectedCharacter = selectedCharacter;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(int selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }
}
