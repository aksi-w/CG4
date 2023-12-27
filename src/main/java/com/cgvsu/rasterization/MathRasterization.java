package com.cgvsu.rasterization;

import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.render_engine.Camera;

public class MathRasterization{
    public static Vector3f getNormal(Point3D p1, Point3D p2, Point3D p3) {
        Vector3f p1p2 = new Vector3f((float) (p2.getX() - p1.getX()), (float) (p2.getY() - p1.getY()), (float) (p2.getZ() - p1.getZ()));
        Vector3f p1p3 = new Vector3f((float) (p3.getX() - p1.getX()), (float) (p3.getY() - p1.getY()), (float) (p3.getZ() - p1.getZ()));
        double A = p1p2.getY() * p1p3.getZ() - p1p2.getZ() * p1p3.getY();
        double B = -(p1p2.getX() * p1p3.getZ() - p1p2.getZ() * p1p3.getX());
        double C = p1p2.getX() * p1p3.getY() - p1p2.getY() * p1p3.getX();
        double normalLength = Math.sqrt(A * A + B * B + C * C);
        return new Vector3f((float) (A / normalLength), (float) (B / normalLength), (float) (C / normalLength));
    }

    public static double getZ(Point3D p1, Point3D p2, Point3D p3, double x, double y) {
        Vector3f normal = getNormal(p1, p2, p3);
        return (-normal.getX() * (x - p1.getX()) - normal.getY() * (y - p1.getY())) / normal.getZ() + p1.getZ();
    }

    public static double getCosLight(Camera camera, Point3D p1, Point3D p2, Point3D p3) {
        Vector3f normal = getNormal(p1, p2, p3);
        Vector3f normalCamera = new Vector3f(
                camera.getTarget().getX() - camera.getPosition().getX(),
                camera.getTarget().getY() - camera.getPosition().getY(),
                camera.getTarget().getZ() - camera.getPosition().getZ());
        Vector3f normalCameraN = new Vector3f(
                normalCamera.getX() / normalCamera.vectorLength(normalCamera),
                normalCamera.getY() / normalCamera.vectorLength(normalCamera),
                normalCamera.getZ() / normalCamera.vectorLength(normalCamera));
        double numerator = normalCameraN.getX() * normal.getX() + normalCameraN.getY() * normal.getY() + normalCameraN.getZ() * normal.getZ();

        double denominator = normal.vectorLength(normal) * normalCameraN.vectorLength(normalCameraN);
        return Math.abs(numerator / denominator);
    }
}
