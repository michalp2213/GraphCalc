package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class CircleVertex extends Vertex<Circle> {
    private Pane parent;

    public CircleVertex(Circle v, Pane parent) {
        super(v);
        this.parent = parent;
    }

    protected CircleVertex(Circle v, long id, Pane parent) {
        super(v, id);
        this.parent = parent;
    }

    @Override
    public void finishIt(){
        parent.getChildren().remove(v);
    }
    
    public SerializableCircleVertex getSerializableVertex() {
    	return new SerializableCircleVertex(new SerializableCircle(v), this.id);
    }
    
    public void draw() {
    	parent.getChildren().add(v);
    }
}
