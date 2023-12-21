package com.cgvsu.math.Vector;

public class Vector3f {

    private final float x;
    private final float y;
    private final float z;
    private static final float eps = 1e-7f;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public boolean equals(Vector3f other) {
        return Math.abs(x - other.getX()) < eps && Math.abs(y - other.getY()) < eps && Math.abs(z - other.getZ()) < eps;
    }

    public static Vector3f addition(Vector3f vector1, Vector3f vector2) {
        float resX = vector1.getX() + vector2.getX();
        float resY = vector1.getY() + vector2.getY();
        float resZ = vector1.getZ() + vector2.getZ();
        return new Vector3f(resX, resY, resZ);
    }

    public static Vector3f subtraction(Vector3f vector1, Vector3f vector2) {
        float resX = vector1.getX() - vector2.getX();
        float resY = vector1.getY() - vector2.getY();
        float resZ = vector1.getZ() - vector2.getZ();
        return new Vector3f(resX, resY, resZ);
    }

    public static float lengthVector(Vector3f vector) {
        return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
    }

    public Vector3f normalize() {
        float length = lengthVector(this);
        if (length > eps) {
            float normalizedX = this.getX() / length;
            float normalizedY = this.getY() / length;
            float normalizedZ = this.getZ() / length;
            return new Vector3f(normalizedX, normalizedY, normalizedZ);
        } else {
            throw new IllegalArgumentException("Деление на 0!");
        }
    }

    public static Vector3f multiplication(Vector3f vector, float a) {
        float resX = vector.getX() * a;
        float resY = vector.getY() * a;
        float resZ = vector.getZ() * a;
        return new Vector3f(resX, resY, resZ);
    }

    public static Vector3f division(Vector3f vector, float a) {
        if (a < eps) {
            throw new IllegalArgumentException("Деление на 0!");
        } else {
            float resX = vector.getX() / a;
            float resY = vector.getY() / a;
            float resZ = vector.getZ() / a;
            return new Vector3f(resX, resY, resZ);
        }
    }

     public static float scalar(Vector3f vector1, Vector3f vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
    }

    public static Vector3f cross(Vector3f vector1, Vector3f vector2) {
        float resX = vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY();
        float resY = vector1.getZ() * vector2.getX() - vector1.getX() * vector2.getZ();
        float resZ = vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX();

        return new Vector3f(resX, resY, resZ);
    }


}
