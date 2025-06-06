// com/syalux/splash/data/CharacterManager.java
package com.syalux.splash.data;

import java.util.*;

public class CharacterManager {
    private static final List<CharacterData> characters = new ArrayList<>();
    private static final Random rand = new Random();

    static {
        initializeCharacters();
    }

    private static void initializeCharacters() {
        for(int i = 1; i <= 19; i++) {
            characters.add(createCharacter(i));
        }
    }

    private static CharacterData createCharacter(int id) {
        return new CharacterData(
            id,
            "Fish " + id,
            generatePrice(id),       // $500-$5000
            generateHealth(id),      // 100-500
            generateSpeed(id),      // 300-800
            generateSize(id),       // 40-100
            id == 1                 // Unlock first character by default
        );
    }

    private static int generatePrice(int id) {
        return 500 + (id-1)*250 + rand.nextInt(200);
    }

    private static int generateHealth(int id) {
        return 100 + (int)(id * 15 * rand.nextDouble());
    }

    private static int generateSpeed(int id) {
        return 300 + (int)(id * 25 * rand.nextDouble());
    }

    private static int generateSize(int id) {
        return 40 + (int)(id * 3 * rand.nextDouble());
    }

    public static List<CharacterData> getAllCharacters() {
        return Collections.unmodifiableList(characters);
    }

    public static CharacterData getCharacter(int id) {
        return characters.stream()
            .filter(c -> c.getId() == id)
            .findFirst()
            .orElse(null);
    }
}