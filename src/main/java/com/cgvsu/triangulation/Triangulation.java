package com.cgvsu.triangulation;

import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.math.Vector.Vector3f.fromTwoPoints;
import static com.cgvsu.math.Vector.Vector3f.sum;

public class Triangulation {

    public static ArrayList<Polygon> triangulation(ArrayList<Polygon> polygons) {
        ArrayList<Polygon> triangles = new ArrayList<>();
        for (Polygon polygon : polygons) {
            int index = 0;
            while (polygon.getVertexIndices().size() > 2) { //Циклическая проверка для триангуляции

                Polygon triangle = new Polygon();

                // Добавляем вершины в треугольник
                triangle.getVertexIndices().add(
                        polygon.getVertexIndices().get(index));
                triangle.getVertexIndices().add(
                        polygon.getVertexIndices().get(index + 1));
                triangle.getVertexIndices().add(
                        polygon.getVertexIndices().get(index + 2));


                if (polygon.getTextureVertexIndices().size() != 0) {
                    triangle.getTextureVertexIndices().add(
                            polygon.getTextureVertexIndices().get(index));
                    triangle.getTextureVertexIndices().add(
                            polygon.getTextureVertexIndices().get(index + 1));
                    triangle.getTextureVertexIndices().add(
                            polygon.getTextureVertexIndices().get(index + 2));

                    polygon.getTextureVertexIndices().remove(index + 1);
                }

                if (polygon.getNormalIndices().size() != 0) {
                    triangle.getNormalIndices().add(
                            polygon.getNormalIndices().get(index));
                    triangle.getNormalIndices().add(
                            polygon.getNormalIndices().get(index + 1));
                    triangle.getNormalIndices().add(
                            polygon.getNormalIndices().get(index + 2));

                    polygon.getNormalIndices().remove(index + 1);
                }


                polygon.getVertexIndices().remove(index + 1);
                triangles.add(triangle);
            }

            if (polygon.getVertexIndices().size() < 3) { //Убираем из массива точки, которые уже построились
                polygon.getVertexIndices().clear();
            }
        }
        return triangles;
    }

    /*public static List<Polygon> triangulation(Polygon polygon) {
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
    }*/
}
