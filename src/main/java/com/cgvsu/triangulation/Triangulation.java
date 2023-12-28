package com.cgvsu.triangulation;

import com.cgvsu.model.Polygon;

import java.util.ArrayList;

public class Triangulation {

    public static ArrayList<Polygon> triangulation(ArrayList<Polygon> polygons) {
        ArrayList<Polygon> triangles = new ArrayList<>();
        for (Polygon polygon : polygons) {
            int index = 0;
            while (polygon.getVertexIndices().size() > 2) { //Циклическая проверка для триангуляции

                Polygon triangle = new Polygon();

                // Добавляем вершины в треугольник
                triangle.getVertexIndices().add(polygon.getVertexIndices().get(index));
                triangle.getVertexIndices().add(polygon.getVertexIndices().get(index + 1));
                triangle.getVertexIndices().add(polygon.getVertexIndices().get(index + 2));


                if (polygon.getTextureVertexIndices().size() != 0) {
                    triangle.getTextureVertexIndices().add(polygon.getTextureVertexIndices().get(index));
                    triangle.getTextureVertexIndices().add(polygon.getTextureVertexIndices().get(index + 1));
                    triangle.getTextureVertexIndices().add(polygon.getTextureVertexIndices().get(index + 2));

                    polygon.getTextureVertexIndices().remove(index + 1);
                }

                if (polygon.getNormalIndices().size() != 0) {
                    triangle.getNormalIndices().add(polygon.getNormalIndices().get(index));
                    triangle.getNormalIndices().add(polygon.getNormalIndices().get(index + 1));
                    triangle.getNormalIndices().add(polygon.getNormalIndices().get(index + 2));

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

}
