package com.github.michalp2213.GraphCalc.Model;

import java.io.Serializable;

/**
 * Class describing vertex.
 */

public class Vertex<T> implements Serializable {
	static final long serialVersionUID = 01L;
	
    protected final T v;

    public Vertex(T v) {
        this.v = v;
    }

    /**
     * Function that will be called before removing edge from graph.
     */

    public void finishIt(){

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex)) return false;
        return ((Vertex) obj).v.equals(v);
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }
}
