package com.cgvsu.math.affinetransf;

import com.cgvsu.math.Matrix.Matrix4f;
import com.cgvsu.math.Vector.Vector3f;

public class VectorMath {
    public static Vector3f mullMatrix4x4OnVector3D(Matrix4f m, Vector3f v) {
        float x = m.getMatrix(0, 0) * v.getX() + m.getMatrix(0, 1) * v.getY() + m.getMatrix(0, 2) * v.getZ() + m.getMatrix(0, 3);
        float y = m.getMatrix(1, 0) * v.getX() + m.getMatrix(1, 1) * v.getY() + m.getMatrix(1, 2) * v.getZ() + m.getMatrix(1, 3);
        float z = m.getMatrix(2, 0) * v.getX() + m.getMatrix(2, 1) * v.getY() + m.getMatrix(2, 2) * v.getZ() + m.getMatrix(2, 3);

        return new Vector3f(x, y, z);
    }
}
