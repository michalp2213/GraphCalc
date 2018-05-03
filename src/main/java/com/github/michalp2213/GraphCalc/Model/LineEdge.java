package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class LineEdge extends Edge<Circle> {

    private Object line;
    private Pane parent;

    public LineEdge(Vertex<Circle> from, Vertex<Circle> to, Object obj, Pane parent) {
        super(from, to);
        line = obj;
        this.parent = parent;
    }

    @Override
    public void finishIt(){
        parent.getChildren().remove(line);
    }
}
