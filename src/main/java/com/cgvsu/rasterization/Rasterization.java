package com.cgvsu.rasterization;

import com.cgvsu.GuiController;
import com.cgvsu.math.Vector.Vector2f;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.Camera;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rasterization {
    public static void fillTriangle(
            final GraphicsUtils gr,
            Point3D p1, Point3D p2, Point3D p3,
            Color color1, Color color2, Color color3,
            Double[][] zBuffer, Camera camera, BufferedImage image,
            Vector2f texturePoint1, Vector2f texturePoint2, Vector2f texturePoint3, Model mesh) throws IOException {

        List<Point3D> points = new ArrayList<>(Arrays.asList(p1, p2, p3));

        if (points.get(0).getY() > points.get(1).getY()) {
            Point3D tmp = points.get(1);
            points.set(1, points.get(0));
            points.set(0, tmp);
            Vector2f tmp1 = texturePoint1;
            texturePoint1 = texturePoint2;
            texturePoint2 = tmp1;
        }
        if (points.get(1).getY() > points.get(2).getY()) {
            Point3D tmp = points.get(2);
            points.set(2, points.get(1));
            points.set(1, tmp);
            Vector2f tmp1 = texturePoint2;
            texturePoint2 = texturePoint3;
            texturePoint3 = tmp1;
            if (points.get(0).getY() > points.get(1).getY()) {
                Point3D tmp2 = points.get(1);
                points.set(1, points.get(0));
                points.set(0, tmp2);
                Vector2f tmp3 = texturePoint1;
                texturePoint1 = texturePoint2;
                texturePoint2 = tmp3;
            }
        }

        double cosLight;
        if (GuiController.isLight) cosLight = MathRasterization.getCosLight(camera, p1, p2, p3);
        else cosLight = 1;
        final double x1 = points.get(0).getX();
        final double x2 = points.get(1).getX();
        final double x3 = points.get(2).getX();
        final double y1 = points.get(0).getY();
        final double y2 = points.get(1).getY();
        final double y3 = points.get(2).getY();
        final double z1 = points.get(0).getZ();
        final double z2 = points.get(1).getZ();
        final double z3 = points.get(2).getZ();

        for (int y = (int) (y1 + 1); y <= y2; y++) {
            double startX = getX(y, x1, x2, y1, y2);
            double endX = getX(y, x1, x3, y1, y3);
            fillLine(gr, y, startX, endX, color1, color2, color3, x1, x2, x3, y1, y2, y3, z1, z2, z3, zBuffer, camera, cosLight, image, texturePoint1, texturePoint2, texturePoint3,mesh);
        }

        for (int y = (int) (y2 + 1); y < y3; y++) {
            double startX = getX(y, x1, x3, y1, y3);
            double endX = getX(y, x2, x3, y2, y3);
            fillLine(gr, y, startX, endX, color1, color2, color3, x1, x2, x3, y1, y2, y3, z1, z2, z3, zBuffer, camera, cosLight, image, texturePoint1, texturePoint2, texturePoint3,mesh);
        }
    }

    public static void fillTriangle(
            GraphicsUtils gr,
            double x1, double y1, double z1,
            double x2, double y2, double z2,
            double x3, double y3, double z3,
            Color color1, Color color2, Color color3,
            Double[][] zBuffer, Camera camera, BufferedImage image,
            Vector2f texturePoint1, Vector2f texturePoint2, Vector2f texturePoint3, Model mesh) throws IOException {
        fillTriangle(gr, new Point3D(x1, y1, z1), new Point3D(x2, y2, z2), new Point3D(x3, y3, z3),
                color1, color2, color3, zBuffer, camera, image, texturePoint1, texturePoint2, texturePoint3,mesh);
    }

    private static double getX(double y, double x1, double x2, double y1, double y2) {
        return (x2 - x1) * (y - y1) / (y2 - y1) + x1;
    }

    private static void fillLine(
            final GraphicsUtils gr, int y, double startX, double endX,
            Color color1, Color color2, Color color3,
            double x1, double x2, double x3,
            double y1, double y2, double y3,
            double z1, double z2, double z3,
            Double[][] zBuffer, Camera camera, double cosLight, BufferedImage image,
            Vector2f texturePoint1, Vector2f texturePoint2, Vector2f texturePoint3, Model mesh) throws IOException {

        if (Double.compare(startX, endX) > 0) {
            double temp = startX;
            startX = endX;
            endX = temp;
        }

        for (int x = (int) startX + 1; x < endX && x < zBuffer.length; x++) {
            double z = MathRasterization.getZ(new Point3D(x1, y1, z1), new Point3D(x2, y2, z2), new Point3D(x3, y3, z3), x, y);

            if (x >= 0 && y >= 0 && y < zBuffer[x].length) {
                if (zBuffer[x][y] == null || zBuffer[x][y] > Math.abs(z - camera.getPosition().getZ())) {
                    double limitedCosLight = Math.max(0, Math.min(1, cosLight));
                    Color color = getColor(color1, color2, color3, x, y, x1, x2, x3, y1, y2, y3, image,
                            texturePoint1, texturePoint2, texturePoint3,mesh);
                    gr.setPixel(x, y, new Color(color.getRed() * limitedCosLight, color.getGreen() * limitedCosLight, color.getBlue() * limitedCosLight));
                    zBuffer[x][y] = Math.abs(z - camera.getPosition().getZ());
                }
            }
        }
    }


    private static Color getColor(
            Color color1, Color color2, Color color3,
            double x, double y,
            double x1, double x2, double x3,
            double y1, double y2, double y3,
            BufferedImage image, Vector2f texturePoint1, Vector2f texturePoint2, Vector2f texturePoint3, Model mesh) throws IOException {
        if (!mesh.isTexture) {
            double detT = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

            double alpha = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / detT;

            double betta = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / detT;

            double gamma = 1 - alpha - betta;

            double r = (alpha * color1.getRed() + betta * color2.getRed() + gamma * color3.getRed());
            double g = (alpha * color1.getGreen() + betta * color2.getGreen() + gamma * color3.getGreen());
            double b = (alpha * color1.getBlue() + betta * color2.getBlue() + gamma * color3.getBlue());

            return new Color(r, g, b);

        } else {
            float aup = (float) (x1 - x);
            float bup = (float) (x2 - x);
            float cup = (float) (x3 - x);
            float avp = (float) (y1 - y);
            float bvp = (float) (y2 - y);
            float cvp = (float) (y3 - y);

            float f =  (1.0f /(float) ((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)));
            float u = (bup * cvp - bvp * cup) * f;
            float v = (cup * avp - cvp * aup) * f;
            float w = 1.0f - (u + v);

            double uI = u * texturePoint1.getX() + v * texturePoint2.getX() + w * texturePoint3.getX();
            double vI = u * texturePoint1.getY() + v * texturePoint2.getY() + w * texturePoint3.getY();
            return getColorTexture(uI, vI, image);
        }
    }

    public static Color getColorTexture(double x0, double y0, BufferedImage image) throws IOException {

        int width = image.getWidth() - 1;
        int height = image.getHeight() - 1;
        int x = (int) (x0 * width);
        int y = (int) (y0 * height);

        int clr = image.getRGB(x, y);
        double red = ((clr & 0x00ff0000) >> 16) / 255.0f;
        double green = ((clr & 0x0000ff00) >> 8) / 255.0f;
        double blue = (clr & 0x000000ff) / 255.0f;
        return new Color(red, green, blue);
    }

}
