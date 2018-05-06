package com.github.michalp2213.GraphCalc.Model;

import java.io.Serializable;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class SerializableCircleVertex extends Vertex<SerializableCircle> implements Serializable {
	static final long serialVersionUID = 01L;
	
	public SerializableCircleVertex(SerializableCircle v) {
		super(v);
	}
	
	public SerializableCircleVertex (Vertex<Circle> v) {
		super(new SerializableCircle(v.v));
	}
	
	public CircleVertex getCircleVertex(Pane parent) {
		return new CircleVertex(v.getCircle(), parent);
	}
}
