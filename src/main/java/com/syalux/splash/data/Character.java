package com.syalux.splash.data;

import java.io.Serializable;

public class Character implements Serializable {
    private static final long serialVersionUID = 2L;

    private final int id;
    private final String name;
    private final int price;
    private final int baseHealth;
    private final int baseSpeed;
    private final int baseSize;
    private boolean unlocked;

    public Character(int id, String name, int price,
                         int baseHealth, int baseSpeed, int baseSize,
                         boolean unlocked) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.baseHealth = baseHealth;
        this.baseSpeed = baseSpeed;
        this.baseSize = baseSize;
        this.unlocked = unlocked;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getBaseHealth() { return baseHealth; }
    public int getBaseSpeed() { return baseSpeed; }
    public int getBaseSize() { return baseSize; }
    public boolean isUnlocked() { return unlocked; }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}