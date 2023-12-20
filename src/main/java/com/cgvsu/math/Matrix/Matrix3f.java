package com.cgvsu.math.Matrix;


import com.cgvsu.math.Vector.Vector3f;

public class Matrix3f {
    private static final float eps = 1e-7f;

    private final float[][] matrix;

    public float getMatrix(int i, int j) {
        return this.matrix[i][j];
    }

    public Matrix3f(float[][] matrix) {
        this.matrix = matrix;
    }

    public boolean equals(Matrix3f otherMatrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (Math.abs(matrix[i][j] - otherMatrix.matrix[i][j]) < eps) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Matrix3f addition(Matrix3f matrix1, Matrix3f matrix2) {
        float[][] res = new float[3][3];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i][j] = matrix1.matrix[i][j] + matrix2.matrix[i][j];
            }
        }
        return new Matrix3f(res);
    }

    public static Matrix3f subtraction(Matrix3f matrix1, Matrix3f matrix2) {
        float[][] res = new float[3][3];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i][j] = matrix1.matrix[i][j] - matrix2.matrix[i][j];
            }
        }
        return new Matrix3f(res);
    }

    public static Matrix3f multiplication(Matrix3f matrix1, Matrix3f matrix2) {
        float[][] res = new float[3][3];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                for (int k = 0; k < res.length; k++) {
                    res[i][j] += matrix1.matrix[i][k] * matrix2.matrix[k][j];
                }
            }
        }
        return new Matrix3f(res);
    }

    public static Matrix3f transposition(Matrix3f matrix) {
        float[][] res = new float[3][3];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i][j] = matrix.matrix[j][i];
            }
        }
        return new Matrix3f(res);
    }

    public static Matrix3f zeroMatrix() {
        float[][] zeroMatrix = new float[3][3];
        return new Matrix3f(zeroMatrix);
    }

    public static Matrix3f unitMatrix() {
        float[][] res = new float[3][3];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                if (i == j) {
                    res[i][j] = 1;
                } else {
                    res[i][j] = 0;
                }
            }
        }
        return new Matrix3f(res);
    }

    public static Vector3f multiplyOnVector(Matrix3f matrix, Vector3f vector) {
        float[] res = new float[3];
        for (int i = 0; i < res.length; i++) {
            res[i] = matrix.matrix[i][0] * vector.getX() +
                    matrix.matrix[i][1] * vector.getY() +
                    matrix.matrix[i][2] * vector.getZ();
        }
        return new Vector3f(res[0], res[1], res[2]);
    }
}

