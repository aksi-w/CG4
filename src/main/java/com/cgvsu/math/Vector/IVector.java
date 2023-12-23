package com.cgvsu.math.Vector;

public interface IVector {
    float getX();

    float getY();

    float getZ();

    float getW();

    void vectorCrossProduct(final Vector v2);
    Vector vectorCrossProduct(final Vector v1,final Vector v2);

    void setValues(final float[] values);

    void sumVectors(final Vector v2);
    Vector sumVectors(final Vector v1, final Vector v2);

    void minusTwoVectors(final Vector v2);
    Vector minusTwoVectors(final Vector v1,final Vector v2);

    void productVectorOnScalar(final float scalar);
    Vector productVectorOnScalar(final Vector v1,final float scalar);

    void divisionVectorOnScalar(final float scalar);
    Vector divisionVectorOnScalar(final Vector v1, final float scalar);

    float vectorLength(final Vector v1);

    Vector vectorNormalization();

    float vectorDotProduct(final Vector v2);

    float[] getValues();

    int getSize();
}
