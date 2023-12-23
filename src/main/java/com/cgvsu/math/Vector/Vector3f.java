package com.cgvsu.math.Vector;

import java.util.List;

public class Vector3f extends Vector implements IVector {

    private float x;
    private float y;
    private float z;
    private static final float eps = 1e-7f;

    public Vector3f() {
        super();
    }

    public Vector3f(float x, float y, float z) {
        this();
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

    @Override
    public float[] getValues() {
        return new float[0];
    }

    @Override
    public int getSize() {
        return 0;
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

    public static Vector3f fromTwoPoints(Vector3f vertex1, Vector3f vertex2) {
        return new Vector3f(vertex2.x - vertex1.x,
                vertex2.y - vertex1.y,
                vertex2.z - vertex1.z);
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public static Vector3f sum(List<Vector3f> vectors) {
        final var result = new Vector3f();

        vectors.forEach(result::add);

        return result;
    }

    public void add(Vector3f t1, Vector3f t2) {
        if (t1 == null || t2 == null) {
            throw new IllegalArgumentException("Vector3f can not be null");
        }

        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public void add(Vector3f t1) {
        if (t1 == null) {
            throw new IllegalArgumentException("Vector3f can not be null");
        }

        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }

    public Vector3f divide(float num) {
        if (isEqual(num, 0))
            throw new ArithmeticException("Division by zero");

        return new Vector3f(x / num, y / num, z / num);
    }

    public static boolean isEqual(float x, float y){
        return Math.abs(x-y) < eps;
    }
}
