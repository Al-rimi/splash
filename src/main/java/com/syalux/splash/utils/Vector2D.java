package com.syalux.splash.utils;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector2D multiply(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }
}
