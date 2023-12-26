package com.cgvsu.model;


import com.cgvsu.math.Vector.Vector2f;
import com.cgvsu.math.Vector.Vector3f;

import java.util.*;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();
    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    public ArrayList<Polygon> trianglePolygons = new ArrayList<Polygon>();
    private String name;

    public boolean isTexture = false;

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector2f> textureVertices, ArrayList<Vector3f> normals, ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }
    public String getName() {
        return name;
    }

    public Model() {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }

    public List<Vector3f> getVertices() {
        return new ArrayList<>(vertices);
    }
    public void addVertex(Vector3f vertex) {
        this.vertices.add(vertex);
    }

    public ArrayList<Vector2f> getTextureVertices() {
        return new ArrayList<>(textureVertices);
    }

    public void addTextureVertex(Vector2f textureVertex) {
        this.textureVertices.add(textureVertex);
    }

    public ArrayList<Vector3f> getNormals() {
        return new ArrayList<>(normals);
    }

    public void addNormal(Vector3f normal) {
        this.normals.add(normal);
    }

    public ArrayList<Polygon> getPolygons() {
        return new ArrayList<>(polygons);
    }
    public void setTrianglePolygons(ArrayList<Polygon> polygons){
        this.trianglePolygons=polygons;
    }

    public void addPolygon(Polygon polygon) {
        this.polygons.add(polygon);
    }
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

}
