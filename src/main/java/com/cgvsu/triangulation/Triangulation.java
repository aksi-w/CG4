package com.cgvsu.triangulation;

import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.math.Vector.Vector3f.fromTwoPoints;
import static com.cgvsu.math.Vector.Vector3f.sum;

public class Triangulation {
    public static List<Polygon> triangulation(Polygon polygon) {
        List<Polygon> triangularPolygons = new ArrayList<>();

        List<Integer> vertexIndices = polygon.getVertexIndices();
        int quantityVertexes = vertexIndices.size();

        List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
        checkForCorrectListSize(textureVertexIndices, quantityVertexes, "текстурных координат");

        List<Integer> normalIndices = polygon.getNormalIndices();
        checkForCorrectListSize(normalIndices, quantityVertexes, "нормалей");


        for (int index = 1; index < vertexIndices.size() - 1; index++) {
            List<Integer> threeVertexIndices = getIndicesListForCurrentPolygon(vertexIndices, index);
            List<Integer> threeTextureVertexIndices = getIndicesListForCurrentPolygon(textureVertexIndices, index);
            List<Integer> threeNormalIndices = getIndicesListForCurrentPolygon(normalIndices, index);

            Polygon triangularPolygon = new Polygon();
            triangularPolygon.setVertexIndices((ArrayList<Integer>) threeVertexIndices);
            triangularPolygon.setTextureVertexIndices((ArrayList<Integer>) threeTextureVertexIndices);
            triangularPolygon.setNormalIndices((ArrayList<Integer>) threeNormalIndices);

            triangularPolygons.add(triangularPolygon);
        }

        return triangularPolygons;
    }

    private static void checkForCorrectListSize(List<Integer> list, int expectedSize, String listName) {
        if (list.size() != 0 && list.size() != expectedSize) {
            throw new IllegalArgumentException("Некорректное количество " + listName + " в полигоне");
        }
    }

    private static List<Integer> getIndicesListForCurrentPolygon(List<Integer> list, int indexSecondVertex) {
        List<Integer> indices = new ArrayList<>();

        if (list.size() != 0) {
            indices.add(list.get(0));
            indices.add(list.get(indexSecondVertex));
            indices.add(list.get(indexSecondVertex + 1));
        }

        return indices;
    }

    public static void recalculateNormals(Model model) {
        model.normals.clear();

        for (int i = 0; i < model.vertices.size(); i++) {
            model.normals.add(calculateNormalForVertexInModel(model, i));
        }
    }

    protected static Vector3f calculateNormalForPolygon(final Polygon polygon, final Model model){

        List<Integer> vertexIndices = polygon.getVertexIndices();
        int verticesCount = vertexIndices.size();

        Vector3f vector1 = fromTwoPoints(model.getVertices().get(vertexIndices.get(0)), model.getVertices().get(vertexIndices.get(1)));
        Vector3f vector2 = fromTwoPoints(model.getVertices().get(vertexIndices.get(0)), model.getVertices().get(vertexIndices.get(verticesCount - 1)));

        Vector3f resultVector = new Vector3f();
        resultVector.cross(vector1, vector2);
        return resultVector;
    }

    protected static Vector3f calculateNormalForVertexInModel(final Model model, final int vertexIndex) {
        List<Vector3f> saved = new ArrayList<>();

        for (Polygon polygon : model.getPolygons()) {
            if (polygon.getVertexIndices().contains(vertexIndex)) {
                Vector3f polygonNormal = calculateNormalForPolygon(polygon, model);
                if (polygonNormal.length() > 0) {
                    saved.add(polygonNormal);
                }
            }
        }

        if (saved.isEmpty()) {
            return new Vector3f();
        }

        return sum(saved).divide(saved.size());
    }
}
