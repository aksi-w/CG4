package com.cgvsu.math.Vector;

public class Vector4f{
    private final float x;
    private final float y;
    private final float z;
    private final float w;
    private static final float eps = 1e-7f;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public float getW() {
        return w;
    }

    public boolean equals(Vector4f other) {
        return Math.abs(x - other.getX()) < eps && Math.abs(y - other.getY()) < eps && Math.abs(z - other.getZ()) < eps && Math.abs(w - other.getW()) < eps;
    }

    public static Vector4f addition(Vector4f vector1, Vector4f vector2) {
        float resX = vector1.getX() + vector2.getX();
        float resY = vector1.getY() + vector2.getY();
        float resZ = vector1.getZ() + vector2.getZ();
        float resW = vector1.getW() + vector2.getW();
        return new Vector4f(resX, resY, resZ, resW);
    }

    public static Vector4f subtraction(Vector4f vector1, Vector4f vector2) {
        float resX = vector1.getX() - vector2.getX();
        float resY = vector1.getY() - vector2.getY();
        float resZ = vector1.getZ() - vector2.getZ();
        float resW = vector1.getW() - vector2.getW();
        return new Vector4f(resX, resY, resZ, resW);
    }

    public static float lengthVector(Vector4f vector) {
        return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ() + vector.getW() * vector.getW());
    }

    public static Vector4f normalize(Vector4f vector) {
        float length = lengthVector(vector);
        if (length > eps) {
            float normalizedX = vector.getX() / length;
            float normalizedY = vector.getY() / length;
            float normalizedZ = vector.getZ() / length;
            float normalizedW = vector.getW() / length;
            return new Vector4f(normalizedX, normalizedY, normalizedZ, normalizedW);
        } else {
            throw new IllegalArgumentException("Деление на 0!");
        }
    }

    public static Vector4f multiplication(Vector4f vector, float a) {
        float resX = vector.getX() * a;
        float resY = vector.getY() * a;
        float resZ = vector.getZ() * a;
        float resW = vector.getW() * a;
        return new Vector4f(resX, resY, resZ, resW);
    }

    public static Vector4f division(Vector4f vector, float a) {
        if (a < eps) {
            throw new IllegalArgumentException("Деление на 0!");
        } else {
            float resX = vector.getX() / a;
            float resY = vector.getY() / a;
            float resZ = vector.getZ() / a;
            float resW = vector.getW() / a;
            return new Vector4f(resX, resY, resZ, resW);

        }
    }

    public static float scalar(Vector4f vector1, Vector4f vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() +
                vector1.getZ() * vector2.getZ() + vector1.getW() * vector2.getW();
    }

}
