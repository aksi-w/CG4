package com.cgvsu.math.affinetransf;
//Аффинные преобразования. В программе реализована только часть графического конвейера. Нет перегонки из локальных координат в мировые координаты сцены. Вам нужно реализовать её, то есть добавить аффинные преобразования: масштабирование, вращение, перенос. Можете использовать наработки
//        студентов из предыдущей задачи. И не забудьте про тесты, без них визуально
//        может быть сложно отследить баги.
//        4. Трансформация модели. После реализации всего конвейера, нужно добавить в
//        меню настройку модели. Необходима возможность масштабировать ее вдоль
//        каждой из осей, вокруг каждой из осей поворачивать и перемещать. При сохранении модели (см. работу другого студента) следует выбирать, учитывать
//        трансформации модели или нет. То есть нужна возможность сохранить как
//        исходную модель, так и модель после преобразований. Посоветуйтесь с человеком, отвечающим за интерфейс, он может выделить вам место под нужные
//        кнопки.


import com.cgvsu.math.Matrix.Matrix4f;
import com.cgvsu.math.Vector.Vector2f;
import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
;

import java.util.ArrayList;

public class AffineTransf {

    //Перечесисление отвечающее за порядок поворотов в каждой из плоскостей
    private OrderRotation orderRotation = OrderRotation.ZYX;

    //Параметры масштабирования
    private float Sx = 1;
    private float Sy = 1;
    private float Sz = 1;
    //Параметры поворота
    //УГЛЫ ПОВОРОТА ЗАДАЮТСЯ ПО ЧАСОВОЙ СРЕЛКЕ В ГРАДУСАХ
    private float Rx = 0;
    private float Ry = 0;
    private float Rz = 0;
    //Параметры переноса
    private float Tx = 0;
    private float Ty = 0;
    private float Tz = 0;

    private Matrix4f R = Matrix4f.unitMatrix();
    private Matrix4f S;
    private Matrix4f T;
    private Matrix4f A = Matrix4f.unitMatrix();

    private final Matrix4f U = Matrix4f.unitMatrix();

    public AffineTransf() {
    }

    public AffineTransf(OrderRotation orderRotation, float sx, float sy, float sz, float rx, float ry, float rz, float tx, float ty, float tz) {
        this.orderRotation = orderRotation;
        Sx = sx;
        Sy = sy;
        Sz = sz;
        Rx = rx;
        Ry = ry;
        Rz = rz;
        Tx = tx;
        Ty = ty;
        Tz = tz;

        calculateA();
    }

    private void calculateA() {
        //Матрица поворота задается единичной
        R = Matrix4f.unitMatrix();

        //Вычисление матрицы переноса
        T = new Matrix4f(new float[][]{{1, 0, 0, Tx},
                {0, 1, 0, Ty},
                {0, 0, 1, Tz},
                {0, 0, 0, 1}});
        //Вычисление матрицы масштабирования
        S = new Matrix4f(new float[][]{{Sx, 0, 0, 0},
                {0, Sy, 0, 0},
                {0, 0, Sz, 0},
                {0, 0, 0, 1}});

        //Вычисление тригонометрических функций
        float sinA = (float) Math.sin(Rx * Math.PI / 180);
        float cosA = (float) Math.cos(Rx * Math.PI / 180);

        float sinB = (float) Math.sin(Ry * Math.PI / 180);
        float cosB = (float) Math.cos(Ry * Math.PI / 180);

        float sinY = (float) Math.sin(Rz * Math.PI / 180);
        float cosY = (float) Math.cos(Rz * Math.PI / 180);

        //Матрицы поворота в каждой из плоскостей
        Matrix4f Z = new Matrix4f(new float[][]{{cosY, sinY, 0, 0},
                {-sinY, cosY, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});

        Matrix4f Y = new Matrix4f(new float[][]{{cosB, 0, sinB, 0},
                {0, 1, 0, 0},
                {-sinB, 0, cosB, 0},
                {0, 0, 0, 1}});

        Matrix4f X = new Matrix4f(new float[][]{{1, 0, 0, 0},
                {0, cosA, sinA, 0},
                {0, -sinA, cosA, 0},
                {0, 0, 0, 1}});

        //Матрица аффинных преобразований принимается равной единице
        A = new Matrix4f(T.getMatrix());

        //Перемножение матриц поворота согласно их порядку
        switch (orderRotation) {
            case ZYX -> {
                R = Matrix4f.multiplication(R, X);
                R = Matrix4f.multiplication(R, Y);
                R = Matrix4f.multiplication(R, Z);
            }
            case ZXY -> {
                R = Matrix4f.multiplication(R, Y);
                R = Matrix4f.multiplication(R, X);
                R = Matrix4f.multiplication(R, Z);
            }
            case YZX -> {
                R = Matrix4f.multiplication(R, X);
                R = Matrix4f.multiplication(R, Z);
                R = Matrix4f.multiplication(R, Y);
            }
            case YXZ -> {
                R = Matrix4f.multiplication(R, Z);
                R = Matrix4f.multiplication(R, X);
                R = Matrix4f.multiplication(R, Y);
            }
            case XZY -> {
                R = Matrix4f.multiplication(R, Y);
                R = Matrix4f.multiplication(R, Z);
                R = Matrix4f.multiplication(R, X);
            }
            case XYZ -> {
                R = Matrix4f.multiplication(R, Z);
                R = Matrix4f.multiplication(R, Y);
                R = Matrix4f.multiplication(R, X);
            }
            default -> R = Matrix4f.multiplication(R, U);
        }
        //Вычисление матрицы аффинных преобразований
        A = Matrix4f.multiplication(A, R);
        A = Matrix4f.multiplication(A, S);
    }

    public Vector3f transformVertex(Vector3f v) {
        return VectorMath.mullMatrix4x4OnVector3D(A, v);
    }

    public Model transformModel(Model m) {
        ArrayList<Polygon> p = new ArrayList<>(m.polygons);
        ArrayList<Vector2f> tV = new ArrayList<>(m.textureVertices);
        //Полигоны и текстурные вершины не изменяются

        ArrayList<Vector3f> vertices = new ArrayList<>();
        for (Vector3f v : m.vertices) {
            vertices.add(transformVertex(v));
        }

        ArrayList<Vector3f> normals = new ArrayList<>();
        for (Vector3f v : m.normals) {
            normals.add(VectorMath.mullMatrix4x4OnVector3D(R, v));
            //На преобразование нормалей влияет только матрица поворота
        }

        return new Model(vertices, tV, normals, p);
    }


    public OrderRotation getOrderRotation() {
        return orderRotation;
    }

    public void setOrderRotation(OrderRotation orderRotation) {
        this.orderRotation = orderRotation;
        calculateA();
    }

    public float getSx() {
        return Sx;
    }

    public void setSx(float sx) {
        Sx = sx;
        calculateA();
    }

    public float getSy() {
        return Sy;
    }

    public void setSy(float sy) {
        Sy = sy;
        calculateA();
    }

    public float getSz() {
        return Sz;
    }

    public void setSz(float sz) {
        Sz = sz;
        calculateA();
    }

    public float getRx() {
        return Rx;
    }

    public void setRx(float rx) {
        Rx = rx;
        calculateA();
    }

    public float getRy() {
        return Ry;
    }

    public void setRy(float ry) {
        Ry = ry;
        calculateA();
    }

    public float getRz() {
        return Rz;
    }

    public void setRz(float rz) {
        Rz = rz;
        calculateA();
    }

    public float getTx() {
        return Tx;
    }

    public void setTx(float tx) {
        Tx = tx;
        calculateA();
    }

    public float getTy() {
        return Ty;
    }

    public void setTy(float ty) {
        Ty = ty;
        calculateA();
    }

    public float getTz() {
        return Tz;
    }

    public void setTz(float tz) {
        Tz = tz;
        calculateA();
    }

    public Matrix4f getR() {
        return R;
    }

    public Matrix4f getS() {
        return S;
    }

    public Matrix4f getT() {
        return T;
    }

    public Matrix4f getA() {
        return A;
    }
}
