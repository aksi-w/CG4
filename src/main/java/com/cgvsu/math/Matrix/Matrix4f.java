package com.cgvsu.math.Matrix;


import com.cgvsu.math.Vector.Vector4f;

public class Matrix4f {
    private final float[][] matrix;

    public float getMatrix(int i, int j) {
        return this.matrix[i][j];
    }

    public float[][] getMatrix() {
        return this.matrix;
    }



    public Matrix4f(float[][] matrix) {
        this.matrix = matrix;
    }


    public boolean equals(Matrix4f otherMatrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != otherMatrix.matrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Matrix4f addition(Matrix4f matrix1, Matrix4f matrix2) {
        float[][] res = new float[4][4];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i][j] = matrix1.matrix[i][j] + matrix2.matrix[i][j];
            }
        }
        return new Matrix4f(res);
    }

    public static Matrix4f subtraction(Matrix4f matrix1, Matrix4f matrix2) {
        float[][] res = new float[4][4];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i][j] = matrix1.matrix[i][j] - matrix2.matrix[i][j];
            }
        }
        return new Matrix4f(res);
    }

    public static Matrix4f multiplication(Matrix4f matrix1, Matrix4f matrix2) {
        float[][] res = new float[4][4];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                for (int k = 0; k < 4; k++) {
                    res[i][j] += matrix1.matrix[i][k] * matrix2.matrix[k][j];
                }
            }
        }
        return new Matrix4f(res);
    }

    public static Matrix4f transposition(Matrix4f matrix) {
        float[][] res = new float[4][4];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i][j] = matrix.matrix[j][i];
            }
        }
        return new Matrix4f(res);
    }

    public Matrix4f zeroMatrix() {
        float[][] zeroMatrix = new float[4][4];
        return new Matrix4f(zeroMatrix);
    }

    public static Matrix4f unitMatrix() {
        float[][] res = new float[4][4];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                if (i == j) {
                    res[i][j] = 1;
                } else {
                    res[i][j] = 0;
                }
            }
        }
        return new Matrix4f(res);
    }

    public static Vector4f multiplyOnVector(Matrix4f matrix, Vector4f vector) {
        float[] res = new float[4];
        for (int i = 0; i < res.length; i++) {
            res[i] = matrix.matrix[0][i] * vector.getX() +
                    matrix.matrix[1][i] * vector.getY() +
                    matrix.matrix[2][i] * vector.getZ() +
                    matrix.matrix[3][i] * vector.getW();
        }
        return new Vector4f(res[0], res[1], res[2], res[3]);
    }

    public static Matrix4f rotate(float angle, float axisX, float axisY, float axisZ) {
        float radians = (float) Math.toRadians(angle);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        float oneMinusCos = 1.0f - cos;

        float[][] rotationMatrix = {
                {cos + axisX * axisX * oneMinusCos, axisX * axisY * oneMinusCos - axisZ * sin, axisX * axisZ * oneMinusCos + axisY * sin, 0},
                {axisY * axisX * oneMinusCos + axisZ * sin, cos + axisY * axisY * oneMinusCos, axisY * axisZ * oneMinusCos - axisX * sin, 0},
                {axisZ * axisX * oneMinusCos - axisY * sin, axisZ * axisY * oneMinusCos + axisX * sin, cos + axisZ * axisZ * oneMinusCos, 0},
                {0, 0, 0, 1}
        };

        return new Matrix4f(rotationMatrix);
    }

}