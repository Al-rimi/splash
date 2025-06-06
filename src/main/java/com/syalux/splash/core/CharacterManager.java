package com.syalux.splash.core;

import java.util.*;

import com.syalux.splash.data.Character;
import com.syalux.splash.data.Profile;

public class CharacterManager {
    private static final List<Character> characters = new ArrayList<>();

    public static void init() {
        if (characters.isEmpty()) {
            for(int i = 1; i <= 19; i++) {
                characters.add(createCharacter(i));
            }
        }
    }

    private static Character createCharacter(int id) {
        Random deterministicRand = new Random(id * 1000L);

        return new Character(
            id,
            "Fish " + id,
            generatePrice(id, deterministicRand),
            generateHealth(id, deterministicRand),
            generateSpeed(id, deterministicRand),
            generateSize(id, deterministicRand),
            id == 1
        );
    }

    private static int generatePrice(int id, Random rand) {
        return 100 + (id-1)*50 + rand.nextInt(200);
    }

    private static int generateHealth(int id, Random rand) {
        return 10 + (int)(id * 15 + rand.nextDouble() * 20);
    }

    private static int generateSpeed(int id, Random rand) {
        return 500 + (int)(id * 25 + rand.nextDouble() * 30);
    }

    private static int generateSize(int id, Random rand) {
        return 50 + (int)(id * 3 + rand.nextDouble() * 5);
    }

    public static List<Character> getAllCharacters() {
        Profile profile = Manager.getProfile();
        if (profile == null) {
            System.err.println("Warning: Attempted to get all characters before profile is loaded. Unlocked status may be incorrect.");
            return Collections.unmodifiableList(characters);
        }
        for (Character character : characters) {
            character.setUnlocked(profile.isCharacterUnlocked(character.getId()));
        }
        return Collections.unmodifiableList(characters);
    }

    public static Character getCharacter(int id) {
        Character character = characters.stream()
            .filter(c -> c.getId() == id)
            .findFirst()
            .orElse(null);

        return character;
    }

    public static void updateCharacterUnlockedStatus(Profile profile) {
        if (profile != null) {
            for (Character character : characters) {
                character.setUnlocked(profile.isCharacterUnlocked(character.getId()));
            }
        }
    }
}