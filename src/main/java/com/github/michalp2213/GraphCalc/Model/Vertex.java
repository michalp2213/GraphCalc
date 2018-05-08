package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * Class describing vertex.
 */

public class Vertex<T> implements Serializable {
    private static long next_id = 0;
    protected long id;
    protected final T v;

    public Vertex(T v) {
        this.v = v;

        this.id = Vertex.next_id;
        ++ next_id;
    }

    protected Vertex(T v, long id) {
        this.v = v;
        this.id = id;
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
        return this.id == ((Vertex) obj).id;
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }

    @Override
    public Vertex<T> clone() {
        Vertex<T> ret = new Vertex<>(this.v);
        ret.id = this.id;

        return ret;
    }
}
