package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix.Matrix4f;
import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.rasterization.Color;
import com.cgvsu.rasterization.GraphicsUtils;
import com.cgvsu.rasterization.Rasterization;
import javafx.scene.canvas.GraphicsContext;

import javax.vecmath.Point2f;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderRasterization {
    public static void render(
            final GraphicsContext graphicsContext,
            final GraphicsUtils graphicsUtils,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            BufferedImage image) throws IOException {

        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix.getMatrix());
        modelViewProjectionMatrix = Matrix4f.multiplication(modelViewProjectionMatrix, viewMatrix);
        modelViewProjectionMatrix = Matrix4f.multiplication(modelViewProjectionMatrix, projectionMatrix);


        final int nPolygons = mesh.trianglePolygons.size();
        Double[][] zBuffer = new Double[width][height];

        for (int i = 0; i < nPolygons; i++) {
            final int nVerticesInPolygon = mesh.trianglePolygons.get(i).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            List<Double> pointsZ = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.vertices.get(mesh.trianglePolygons.get(i).getVertexIndices().get(vertexInPolygonInd));

                Vector3f vertexVecmath = new Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());
                pointsZ.add((double) vertex.getZ());

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                resultPoints.add(resultPoint);
            }


            Rasterization.fillTriangle(graphicsUtils,
                    resultPoints.get(0).x, resultPoints.get(0).y, pointsZ.get(0),
                    resultPoints.get(1).x, resultPoints.get(1).y, pointsZ.get(1),
                    resultPoints.get(2).x, resultPoints.get(2).y, pointsZ.get(2),
                    Color.BLUE, Color.BLUE, Color.BLUE, zBuffer, camera, image,
                    mesh.textureVertices.get(mesh.trianglePolygons.get(i).getTextureVertexIndices().get(0)),
                    mesh.textureVertices.get(mesh.trianglePolygons.get(i).getTextureVertexIndices().get(1)),
                    mesh.textureVertices.get(mesh.trianglePolygons.get(i).getTextureVertexIndices().get(2)),mesh);
        }
        for (Double[] doubles : zBuffer) {
            Arrays.fill(doubles, null);
        }
    }

}
