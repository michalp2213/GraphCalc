package com.github.michalp2213.GraphCalc.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class describing vertex.
 */

public class Vertex implements Serializable {
    private final int v;
    private HashMap<Class<? extends Attribute>, Attribute> attributes;

    public Vertex(int v) {
        this.v = v;
        attributes = new HashMap<>();
    }

    public int getLabel() {
        return v;
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
        if (!(obj instanceof Vertex)) return false;
        return ((Vertex) obj).v == v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v);
    }
}
