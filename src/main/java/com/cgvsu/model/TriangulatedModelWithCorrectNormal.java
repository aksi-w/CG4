package com.cgvsu.model;

import com.cgvsu.triangulation.Triangulation;

import java.util.ArrayList;
import java.util.List;

public class TriangulatedModelWithCorrectNormal {
    private final Model initialModel;
    private final List<Polygon> triangulatedPolygons;

    public TriangulatedModelWithCorrectNormal(Model initialModel) {
        Triangulation.recalculateNormals(initialModel);
        this.initialModel = initialModel;
        this.triangulatedPolygons = triangulatePolygons(initialModel.polygons);
    }

    public Model getInitialModel() {
        return initialModel;
    }

    public List<Polygon> getTriangulatedPolygons() {
        return triangulatedPolygons;
    }

    public static List<Polygon> triangulatePolygons(List<Polygon> initialPolygons) {
        List<Polygon> triangulationPolygons = new ArrayList<>();

        for (Polygon polygon : initialPolygons) {
            triangulationPolygons.addAll(Triangulation.triangulation(polygon));
        }

        return triangulationPolygons;
    }
}
