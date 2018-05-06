package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class LineEdge extends Edge<Circle> {

    public Node line;
    public Pane parent;

    public LineEdge(Vertex<Circle> from, Vertex<Circle> to, Node obj, Pane parent) {
        super(from, to);
        line = obj;
        this.parent = parent;
    }

    public LineEdge(Vertex<Circle> from, Vertex<Circle> to, HashMap<Class, Object> attributes, Node obj, Pane parent) {
        super(from, to, attributes);
        line = obj;
        this.parent = parent;
    }

    @Override
    public LineEdge transpose() {
        return new LineEdge(to, from, attributes, line, parent);
    }

    @Override
    public void finishIt() {
        parent.getChildren().remove(line);
    }
    
    public void draw() {
    	parent.getChildren().add((Node)line);
    }
}