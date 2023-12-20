package com.cgvsu.math.Vector;

public class Vector2f {

    private final float x;
    private final float y;
    private static final float eps = 1e-7f;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean equals(Vector2f other) {
        return Math.abs(x - other.getX()) < eps && Math.abs(y - other.getY()) < eps;
    }

    public static Vector2f addition(Vector2f vector1, Vector2f vector2) {
        float resX = vector1.getX() + vector2.getX();
        float resY = vector1.getY() + vector2.getY();
        return new Vector2f(resX, resY);
    }

    public static Vector2f subtraction(Vector2f vector1, Vector2f vector2) {
        float resX = vector1.getX() - vector2.getX();
        float resY = vector1.getY() - vector2.getY();
        return new Vector2f(resX, resY);
    }

    public static float lengthVector(Vector2f vector) {
        return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
    }

    public static Vector2f normalize(Vector2f vector) {
        float length = lengthVector(vector);
        if (length > eps) {
            float normalizedX = vector.getX() / length;
            float normalizedY = vector.getY() / length;
            return new Vector2f(normalizedX, normalizedY);
        } else {
            throw new IllegalArgumentException("Деление на 0!");
        }
    }

    public static Vector2f multiplication(Vector2f vector, float a) {
        float resX = vector.getX() * a;
        float resY = vector.getY() * a;
        return new Vector2f(resX, resY);
    }

    public static Vector2f division(Vector2f vector, float a) {
        if (a > eps) {
            float resX = vector.getX() / a;
            float resY = vector.getY() / a;
            return new Vector2f(resX, resY);
        } else {
            throw new IllegalArgumentException("Деление на 0!");
        }
    }

    public static float scalar(Vector2f vector1, Vector2f vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
    }

}
