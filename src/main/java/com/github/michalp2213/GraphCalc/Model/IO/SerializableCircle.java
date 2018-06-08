package com.github.michalp2213.GraphCalc.Model.IO;

import javafx.scene.shape.Circle;

import java.io.Serializable;

public class SerializableCircle implements Serializable {

    private double x;
    private double y;
    private double radius;

    public SerializableCircle (Circle c) {
        x = c.getCenterX();
        y = c.getCenterY();
        radius = c.getRadius();
    }

    public Circle getCircle() {
        return new Circle(x, y, radius);
    }
}
