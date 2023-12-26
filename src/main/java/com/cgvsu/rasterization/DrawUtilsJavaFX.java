package com.cgvsu.rasterization;

import javafx.scene.canvas.Canvas;

public class DrawUtilsJavaFX extends GraphicsUtils<Canvas> {
    public DrawUtilsJavaFX(Canvas graphics) {
        super(graphics);
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        graphics.getGraphicsContext2D().getPixelWriter().setColor(x, y, toColor(color));
    }

    private javafx.scene.paint.Color toColor(Color color) {
        return javafx.scene.paint.Color.color(color.getRed(), color.getGreen(), color.getBlue());
    }


}
