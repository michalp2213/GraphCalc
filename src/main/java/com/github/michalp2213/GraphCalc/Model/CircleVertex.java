package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class CircleVertex extends Vertex<Circle> {
    private Pane parent;

    public CircleVertex(Circle v, Pane parent) {
        super(v);
        this.parent = parent;
    }

    @Override
    public void finishIt(){
        parent.getChildren().remove(v);
    }
    
    public SerializableCircleVertex getSerializableVertex() {
    	return new SerializableCircleVertex(new SerializableCircle(v));
    }
    
    public void draw() {
    	parent.getChildren().add(v);
    }

    @Override
    public String toString() {
        return v.toString();
    }

    /*@Override
    public boolean equals (Object o) {
        if (o instanceof CircleVertex)
            return (this.getSerializableVertex()).equals(((CircleVertex) o).getSerializableVertex());
        return false;
    }

    @Override
    public int hashCode() {
        return this.getSerializableVertex().hashCode();
    }*/
}
