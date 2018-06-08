package com.github.michalp2213.GraphCalc.Model.Graph;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class describing edge.
 */

public class Edge implements Serializable {
    public final Vertex from;
    public final Vertex to;
    private HashMap<Class<? extends Attribute>, Attribute> attributes;

    /**
     * Basic constructor.
     */

    public Edge(Vertex from, Vertex to) {
        this(from, to, new HashMap<>());
    }

    /**
     * Constructor for edge with attributes.
     */

    public Edge(Vertex from, Vertex to, HashMap<Class<? extends Attribute>, Attribute> attributes) {
        this.from = from;
        this.to = to;
        this.attributes = new HashMap<>(attributes);
    }

    public Edge transpose() {
        return new Edge(to, from, attributes);
    }

    /**
     * Adding new attribute, or changing existing.
     */

    public <T extends Attribute> void setAttribute(Class<T> cl, T attribute) {
        attributes.put(cl, attribute);
    }

    @SuppressWarnings("unchecked")
    public <T extends Attribute> T getAttribute(Class<T> cl) {
        return (T) attributes.get(cl);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) return false;
        Edge e = (Edge) obj;
        return e.to.equals(to) && e.from.equals(from) && e.attributes.equals(attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, from, to);
    }
}
