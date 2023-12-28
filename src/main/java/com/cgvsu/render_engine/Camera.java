package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix.Matrix3f;
import com.cgvsu.math.Matrix.Matrix4f;
import com.cgvsu.math.Vector.Vector3f;

import static com.cgvsu.math.Matrix.Matrix4f.rotate;
import static com.cgvsu.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;


public class Camera {
    private static final float ZOOM_SPEED = 0.1f;
    public Camera(
            final Vector3f position,
            final Vector3f target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void movePosition(final Vector3f translation) {
        this.position = Vector3f.addition(position, translation);
    }
    Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }
    Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    Vector3f resultY = new Vector3f(0,1.0f,0);
    public Vector3f vectorX(){
        Vector3f resultX = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ = Vector3f.subtraction(target, position).normalize();
        resultX = Vector3f.cross(resultY, resultZ).normalize();
        resultY = Vector3f.cross(resultZ, resultX).normalize();

        return resultX;
    }


    public void handleMouseScroll(float delta) {
        Vector3f viewDirection = Vector3f.subtraction(target, position).normalize();
        Vector3f newPosition = Vector3f.addition(position, Vector3f.multiplication(viewDirection, delta * ZOOM_SPEED));

        position = newPosition;
    }

    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    private double mousePosX;
    private double mousePosY;
    public double mouseDeltaY;


    public void handleMouseInput(double x, double y, boolean isPrimaryButtonDown, boolean isSecondaryButtonDown) {

        if (isPrimaryButtonDown) {
// Вращение камеры вокруг объекта при зажатой левой кнопке мыши
            rotateCamera((float) (x - mousePosX), (float) (y - mousePosY));
        } else if (isSecondaryButtonDown) {
// Передвижение камеры влево/вправо при зажатой правой кнопке мыши
            movePosition(new Vector3f((float) (x - mousePosX) * 0.1f, (float) (+y - mousePosY) * 0.1f, 0));
        } else {
// Передвижение камеры в зависимости от движения колесика мыши
            if (mouseDeltaY > 0) {
                Vector3f lala = Vector3f.division(target, 75);
                position.subtractThis(Vector3f.subtraction(position, lala));//p-=p-lala=lala
            } else if (mouseDeltaY < 0) {
                Vector3f lala = Vector3f.division(target, 75);
                position.addThis(Vector3f.subtraction(position, lala));
            }
            mouseDeltaY = 0;
        }


        mousePosX = x;
        mousePosY = y;
    }


    private void rotateCamera(float dx, float dy) {
        float rotationX = -dy * 0.2f;
        float rotationY = -dx * 0.2f;

        Matrix4f rotationMatrixX = rotate(rotationX, 1, 0, 0);
        Matrix4f rotationMatrixY = rotate(rotationY, 0, 1, 0);

        Matrix4f rotationMatrix = Matrix4f.multiplication(rotationMatrixX, rotationMatrixY);

        position = multiplyMatrix4ByVector3(rotationMatrix, position);
    }


}