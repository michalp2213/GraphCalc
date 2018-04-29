package Model;

import java.util.Objects;

/*
 * Class describing edge.
 */

public class Edge<T> {
    public final Vertex<T> from;
    public final Vertex<T> to;
    public final int weight;

    /*
     * Basic constructor.
     */

    public Edge(Vertex<T> from, Vertex<T> to) {
        this(from, to, 1);
    }

    /*
     * Constructor for weighted edge.
     */

    public Edge(Vertex<T> from, Vertex<T> to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Edge<T> transpose() {
        return new Edge<>(to, from, weight);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) return false;
        Edge e = (Edge) obj;
        return e.weight == weight && e.to.equals(to) && e.from.equals(from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, from, to);
    }
}
