package Model;

import java.util.Objects;

/*
    Class describing edge.
 */

public class Edge<T> {
    private final Vertex<T> from;
    private final Vertex<T> to;
    private final int weight;

    public Edge(Vertex<T> from, Vertex<T> to) { //Basic constructor.
        this(from, to, 1);
    }

    public Edge(Vertex<T> from, Vertex<T> to, int weight) { //Constructor for weighted edge.
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Edge<T> transpose() {
        return new Edge<>(to, from, weight);
    }

    public Vertex<T> getFrom() {
        return from;
    }

    public Vertex<T> getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
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
