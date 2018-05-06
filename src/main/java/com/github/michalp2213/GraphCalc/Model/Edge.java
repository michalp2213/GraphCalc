package com.github.michalp2213.GraphCalc.Model;

import java.util.HashMap;
import java.util.Objects;

/*
 * Class describing edge.
 */

public class Edge<T> {
    public final Vertex<T> from;
    public final Vertex<T> to;
    protected HashMap<Class, Object> attributes;

    /**
     * Basic constructor.
     */

    public Edge(Vertex<T> from, Vertex<T> to) {
        this(from, to, new HashMap<>());
    }

    /**
     * Constructor for edge with attributes.
     */

    public Edge(Vertex<T> from, Vertex<T> to, HashMap<Class, Object> attributes) {
        this.from = from;
        this.to = to;
        this.attributes = new HashMap<>(attributes);
    }

    public Edge<T> transpose() {
        return new Edge<>(to, from, attributes);
    }

    /**
     * Adding new attribute, or changing existing.
     */

    public void setAttribute(Object o) {
        attributes.put(o.getClass(), o);
    }

    public Object getAttribute(Class cl){
        return attributes.get(cl);
    }

    /**
     * Function that will be called before removing edge from graph.
     */

    public void finishIt(){
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) return false;
        Edge e = (Edge) obj;
        return e.attributes.equals(attributes) && e.to.equals(to) && e.from.equals(from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, from, to);
    }
}