package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.shape.Circle;

import java.io.Serializable;

public class SerializableCircle implements Serializable {
	private double x;
	private double y;
	private double radius;
	
	public SerializableCircle(double x, double y, double r) {
		this.x = x;
		this.y = y;
		this.radius = r;
	}
	
	public SerializableCircle(Circle c) {
		x = c.getCenterX();
		y = c.getCenterY();
		radius = c.getRadius();
	}
	
	public Circle getCircle () {
		return new Circle(x,  y,  radius);
	}

	/*@Override
	public boolean equals(Object o) {
	    if (o instanceof SerializableCircle) {
	        return (x == ((SerializableCircle) o).x) && (y == ((SerializableCircle) o).y);
        }
        return false;
    }

    @Override
    public int hashCode () {
	    return (int) (x + 37*y);
    }*/
}
