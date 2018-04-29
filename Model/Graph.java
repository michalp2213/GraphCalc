package Model;

/*
 * Graph interface describing basic operations on graph.
 */

public interface Graph<T> {
    void addVertex(Vertex<T> v);

    void addEdge(Edge<T> e);

    void removeVertex(Vertex<T> v);

    void removeEdge(Edge<T> e);

    boolean containsVertex(Vertex<T> v);

    boolean containsEdge(Edge<T> e);

}
