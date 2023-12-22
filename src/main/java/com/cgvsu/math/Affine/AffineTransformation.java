package com.cgvsu.math.Affine;

import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.model.Model;

import java.util.ArrayList;

public class AffineTransformation {
    public static double[][] getScaleMatrix(double sx, double sy, double sz) {
        return new double[][]{{sx, 0, 0}, {0, sy, 0}, {0, 0, sz}};
    }

    public static void scale(Model model, double sx, double sy, double sz) {
        ArrayList<Vector3f> resultVertices = new ArrayList<>();
        for (Vector3f vertex : model.vertices) {
            double[][] resultCoords = MathUtils.multiplyMatrices(getScaleMatrix(sx, sy, sz),
                    new double[][]{{vertex.getX()}, {vertex.getY()}, {vertex.getZ()}});

            resultVertices.add(new Vector3f((float) resultCoords[0][0],
                    (float) resultCoords[1][0], (float) resultCoords[2][0]));
        }

        model.vertices = resultVertices;
    }

    public static double[][] getRotateMatrix(double xc, double yc, double zc) {
        double sx = Math.sin(xc);
        double cx = Math.cos(xc);
        double sy = Math.sin(yc);
        double cy = Math.cos(yc);
        double sz = Math.sin(zc);
        double cz = Math.cos(zc);

        return new double[][]{{cx * cy, cx * sy, sx},
                {-sz * sx * cy - cz * sy, -sz * sx * sy + cz * sy, sz * cx},
                {-cz * sx * cy + sz * sy, -cz * sx * sy - sz * sy, cz * cx}};
    }

    public static void rotate(Model model, double xc, double yc, double zc) {
        ArrayList<Vector3f> resultVertices = new ArrayList<>();
        for (Vector3f vertex : model.vertices) {
            double[][] resultCoords = MathUtils.multiplyMatrices(getRotateMatrix(xc, yc, zc),
                    new double[][]{{vertex.getX()}, {vertex.getY()}, {vertex.getZ()}});

            resultVertices.add(new Vector3f((float) resultCoords[0][0],
                    (float) resultCoords[1][0], (float) resultCoords[2][0]));
        }

        model.vertices = resultVertices;
    }

    public static double[][] getTranslateMatrix(double tx, double ty, double tz) {
        return new double[][]{{1, 0, 0, tx}, {0, 1, 0, ty}, {0, 0, 1, tz}, {0, 0, 0, 1}};
    }

    public static void translate(Model model, double tx, double ty, double tz) {
        ArrayList<Vector3f> resultVertices = new ArrayList<>();
        for (Vector3f vertex : model.vertices) {
            double[][] resultCoords = MathUtils.multiplyMatrices(getTranslateMatrix(tx, ty, tz),
                    new double[][]{{vertex.getX()}, {vertex.getY()}, {vertex.getZ()}, {1}});

            resultVertices.add(new Vector3f((float) resultCoords[0][0],
                    (float) resultCoords[1][0], (float) resultCoords[2][0]));
        }

        model.vertices = resultVertices;
    }
}

