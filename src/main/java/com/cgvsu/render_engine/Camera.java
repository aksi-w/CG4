package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix.Matrix3f;
import com.cgvsu.math.Matrix.Matrix4f;
import com.cgvsu.math.Vector.Vector3f;


public class Camera {
    private static final float MOVE_SPEED = 0.5f;
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

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public void setTarget(final Vector3f target) {
        this.target = target;
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


    public void moveTarget(final Vector3f translation) {
        this.target = Vector3f.addition(target, translation);
    }

    Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public void scalePosition(final Vector3f scale){
        //this.position = AffineTransformation.scale(this.position, scale.getX(), scale.getY(), scale.getZ());
    }

    public void rotationPositionAroundX(final int angle){
        //this.position = affineTransform.rotationAroundX(angle,this.position);
    }
    public void rotationPositionAroundY(final int angle){
        //this.position = affineTransform.rotationAroundY(angle,this.position);
    }


    Vector3f resultY = new Vector3f(0,1.0f,0);
    public Vector3f vectorY(){
        Vector3f resultX = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ = Vector3f.subtraction(target, position).normalize();
        resultX = Vector3f.cross(resultY, resultZ).normalize();
        resultY = Vector3f.cross(resultZ, resultX).normalize();

        return resultY;
    }
    public Vector3f vectorZ(){
        Vector3f resultX = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ = Vector3f.subtraction(target, position);
        resultX = Vector3f.cross(resultY, resultZ);
        resultY = Vector3f.cross(resultZ, resultX);


        return resultZ;
    }
    public Vector3f vectorX(){
        Vector3f resultX = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ = Vector3f.subtraction(target, position).normalize();
        resultX = Vector3f.cross(resultY, resultZ).normalize();
        resultY = Vector3f.cross(resultZ, resultX).normalize();

        return resultX;
    }

    public void rotationAroundChangedX(double angle){
        Vector3f resultX = vectorX();
        rotationAroundVector(angle, resultX);
    }

    public void rotationAroundChangedY(double angle){

        resultY = vectorY();
        rotationAroundVector(angle, resultY);
    }

    private void rotationAroundVector(double angle, Vector3f result) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        result.normalize();
        float x = result.getX();
        float y = result.getY();
        float z = result.getZ();
        Matrix3f mRotationAroundAxes = new Matrix3f(
                new float[][]{
                        {cos + (1-cos) * x*x, (1-cos)*x*y - sin*z, (1-cos)*x*z + sin *y},
                        {(1-cos)*y*x + sin*z, cos+(1-cos) * y*y, (1-cos) * y*z - sin*x},
                        {(1-cos)*z*x - sin*y, (1-cos)*z*y + sin*x, cos+(1-cos) * z * z}
                }
        );

        this.position = Matrix3f.multiplyOnVector(mRotationAroundAxes, this.position);
    }


    public void rotationAroundAxes(double angleX, double angleY, double angleZ){
        float sinX = (float) Math.sin(angleX);
        float sinY = (float) Math.sin(angleY);
        float sinZ = (float) Math.sin(angleZ);
        float cosX = (float) Math.cos(angleX);
        float cosY = (float) Math.cos(angleY);
        float cosZ = (float) Math.cos(angleZ);
        Matrix3f mRotationAroundAxes = new Matrix3f(
                new float[][]{
                        {cosY * cosZ, sinY * sinX - cosY * sinZ * cosX, cosY * sinZ * sinX + sinY * cosX},
                        {sinZ, cosY * cosX, -cosZ * sinX},
                        {-sinY * cosZ, sinX * sinZ * cosX + cosY * sinX, cosY * cosX - sinY * sinZ * sinX}
                }
        );
        this.position = Matrix3f.multiplyOnVector(mRotationAroundAxes, this.position);

    }

    public void handleMouseScroll(float delta) {
        // Zoom in or out based on the mouse wheel movement
        Vector3f viewDirection = Vector3f.subtraction(target, position).normalize();
        position = Vector3f.addition(position, Vector3f.multiplication(viewDirection, delta * ZOOM_SPEED));
    }

    public void handleKeyPress(String direction) {
        Vector3f right = Vector3f.cross(target, new Vector3f(0, 1, 0)).normalize();
        Vector3f up = new Vector3f(0, 1, 0);


        switch (direction) {
            case "LEFT" -> position = Vector3f.addition(position, Vector3f.multiplication(right, MOVE_SPEED));
            case "RIGHT" -> position = Vector3f.subtraction(position, Vector3f.multiplication(right, MOVE_SPEED));
            case "UP" -> position = Vector3f.addition(position, Vector3f.multiplication(up, MOVE_SPEED));
            case "DOWN" -> position = Vector3f.subtraction(position, Vector3f.multiplication(up, MOVE_SPEED));
        }
    }

    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

}