package com.cgvsu.math.Affine;

public class MathUtils {
    public static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int m1Rows = matrix1.length;
        int m1Cols = matrix1[0].length;
        int m2Rows = matrix2.length;
        int m2Cols = matrix2[0].length;

        if (m1Cols != m2Rows) {
            throw new IllegalArgumentException("Number of columns in the first matrix must be equal to the number of rows in the second matrix");
        }

        double[][] result = new double[m1Rows][m2Cols];

        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                for (int k = 0; k < m1Cols; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return result;
    }
}
