package com.cgvsu.math.Vector;

public abstract class Vector {
    public static class VectorException extends Exception {
        public VectorException(String message) {
            super(message);
        }
    }

    static final float EPS = 1e-5f;
    protected final int size;
    protected float[] vector;

    public Vector(float[] vector, final int size) {
        if (vector.length == size) {
            this.vector = vector;
            this.size = size;
        } else if (size > 0) {
            float[] rightVector = new float[size];
            System.arraycopy(vector, 0, rightVector, 0, Math.min(vector.length, size));
            this.vector = rightVector;
            this.size = size;
        } else {
            this.vector = new float[0];
            this.size = 0;
        }
    }

    public Vector(float v1, float v2, int size) {
        float[] newVector = new float[size];
        newVector[0] = v1;
        newVector[1] = v2;
        this.vector = newVector;
        this.size = size;
    }

    public Vector(float v1, float v2, float v3, int size) {
        float[] newVector = new float[size];
        newVector[0] = v1;
        newVector[1] = v2;
        newVector[2] = v3;
        this.vector = newVector;
        this.size = size;
    }

    public float vectorLength(final IVector v1) {
        float tmp = 0;

        for (int i = 0; i < v1.getSize(); i++) {
            tmp = tmp + v1.getValues()[i] * v1.getValues()[i];
        }

        return (float) Math.sqrt(tmp);
    }
}
