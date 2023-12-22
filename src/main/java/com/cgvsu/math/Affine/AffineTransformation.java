package com.cgvsu.math.Affine;

import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.model.Model;

import java.util.ArrayList;

public class AffineTransformation {
    public static float[][] getScaleMatrix(float sx, float sy, float sz) {
        return new float[][]{{sx, 0, 0}, {0, sy, 0}, {0, 0, sz}};
    }

    public static void scale(Model model, float sx, float sy, float sz) {
        ArrayList<Vector3f> resultVertices = new ArrayList<>();
        for (Vector3f vertex : model.vertices) {
            float[][] resultCoords = MathUtils.multiplyMatrices(getScaleMatrix(sx, sy, sz),
                    new float[][]{{vertex.getX()}, {vertex.getY()}, {vertex.getZ()}});

            resultVertices.add(new Vector3f(resultCoords[0][0], resultCoords[1][0], resultCoords[2][0]));
        }

        model.vertices = resultVertices;
    }

    public static float[][] getRotateMatrix(float xc, float yc, float zc) {
        float sx = (float) Math.sin(xc);
        float cx = (float) Math.cos(xc);
        float sy = (float) Math.sin(yc);
        float cy = (float) Math.cos(yc);
        float sz = (float) Math.sin(zc);
        float cz = (float) Math.cos(zc);

        return new float[][]{{cx * cy, cx * sy, sx},
                {-sz * sx * cy - cz * sy, -sz * sx * sy + cz * sy, sz * cx},
                {-cz * sx * cy + sz * sy, -cz * sx * sy - sz * sy, cz * cx}};
    }

    public static void rotate(Model model, float xc, float yc, float zc) {
        ArrayList<Vector3f> resultVertices = new ArrayList<>();
        for (Vector3f vertex : model.vertices) {
            float[][] resultCoords = MathUtils.multiplyMatrices(getRotateMatrix(xc, yc, zc),
                    new float[][]{{vertex.getX()}, {vertex.getY()}, {vertex.getZ()}});

            resultVertices.add(new Vector3f(resultCoords[0][0], resultCoords[1][0], resultCoords[2][0]));
        }

        model.vertices = resultVertices;
    }

    public static float[][] getTranslateMatrix(float tx, float ty, float tz) {
        return new float[][]{{1, 0, 0, tx}, {0, 1, 0, ty}, {0, 0, 1, tz}, {0, 0, 0, 1}};
    }

    public static void translate(Model model, float tx, float ty, float tz) {
        ArrayList<Vector3f> resultVertices = new ArrayList<>();
        for (Vector3f vertex : model.vertices) {
            float[][] resultCoords = MathUtils.multiplyMatrices(getTranslateMatrix(tx, ty, tz),
                    new float[][]{{vertex.getX()}, {vertex.getY()}, {vertex.getZ()}, {1}});

            resultVertices.add(new Vector3f(resultCoords[0][0], resultCoords[1][0], resultCoords[2][0]));
        }

        model.vertices = resultVertices;
    }
}
