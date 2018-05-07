package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * Class describing vertex.
 */

public class Vertex<T> implements Serializable {
    protected final T v;

    public Vertex(T v) {
        this.v = v;
    }

    /**
     * Function that will be called before removing edge from graph.
     */

    public void finishIt(){

    }
    
    public T getLabel () {
    	return v;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex)) return false;
        /*if (((Vertex) obj).v instanceof Circle && v instanceof Circle) { //temporary workaround
            return ((Circle)((Vertex) obj).v).getCenterX() == ((Circle)v).getCenterX() &&
                    ((Circle)((Vertex) obj).v).getCenterY() == ((Circle) v).getCenterY();
        }*/
        return ((Vertex) obj).v.equals(v);
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }
}
