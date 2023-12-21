package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix.Matrix4f;
import com.cgvsu.math.Vector.Vector3f;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public class GraphicConveyor {

    public static Matrix4f rotateScaleTranslate() {
        float[][] matrix = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
        return new Matrix4f(matrix);
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f forward = Vector3f.subtraction(target, eye).normalize();
        Vector3f right = Vector3f.cross(up, forward).normalize();
        Vector3f newUp = Vector3f.cross(forward, right).normalize();

        float[][] matrixData = new float[][]{
                {right.getX(), newUp.getX(), -forward.getX(), 0},
                {right.getY(), newUp.getY(), -forward.getY(), 0},
                {right.getZ(), newUp.getZ(), -forward.getZ(), 0},
                {-Vector3f.scalar(right, eye), -Vector3f.scalar(newUp, eye), Vector3f.scalar(forward, eye), 1}
        };

        return new Matrix4f(matrixData);
    }


    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        return new Matrix4f(new float[][]{
                {tangentMinusOnDegree / aspectRatio, 0, 0, 0},
                {0, tangentMinusOnDegree, 0, 0},
                {0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), 1.0F},
                {0, 0, 2 * (nearPlane * farPlane) / (nearPlane - farPlane), 0}
    });
}

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final Vector3f vertex) {
        final float x = (vertex.getX() * matrix.getMatrix(0, 0)) + (vertex.getY() * matrix.getMatrix(1, 0)) +
                (vertex.getZ() * matrix.getMatrix(2, 0)) + matrix.getMatrix(3, 0);
        final float y = (vertex.getX() * matrix.getMatrix(0, 1)) + (vertex.getY() * matrix.getMatrix(1, 1)) +
                (vertex.getZ() * matrix.getMatrix(2, 1)) + matrix.getMatrix(3, 1);
        final float z = (vertex.getX() * matrix.getMatrix(0, 2)) + (vertex.getY() * matrix.getMatrix(1, 2)) +
                (vertex.getZ() * matrix.getMatrix(2, 2)) + matrix.getMatrix(3, 2);
        final float w = (vertex.getX() * matrix.getMatrix(0, 3)) + (vertex.getY() * matrix.getMatrix(1, 3)) +
                (vertex.getZ() * matrix.getMatrix(2, 3)) + matrix.getMatrix(3, 3);

        return new Vector3f(x / w, y / w, z / w);
    }


    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
    public static Point3f vertexToPoint3f(final javax.vecmath.Vector3f vertex, final int width, final int height) {
        return new Point3f(vertex.x * width + width / 2.0F, -vertex.y * height + height / 2.0F, vertex.z * width + width / 2.0F);
    }
}
