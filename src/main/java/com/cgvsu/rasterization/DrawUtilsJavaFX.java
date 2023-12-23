package com.cgvsu.rasterization;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class DrawUtilsJavaFX extends GraphicsUtils<Canvas> {
    public DrawUtilsJavaFX(Canvas graphics) {
        super(graphics);
    }

    @Override
    public void setPixel(int x, int y, Colorr color) {
        graphics.getGraphicsContext2D().getPixelWriter().setColor(x, y, toColor(color));
    }

    private Color toColor(Colorr color) {
        return Color.color(color.getRed(), color.getGreen(), color.getBlue());
    }


}
