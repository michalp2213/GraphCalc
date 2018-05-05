package com.github.michalp2213.GraphCalc.Model;

/*
 * Graph interface describing basic operations on graph.
 */

import java.util.HashSet;
import java.util.Map;

public interface Graph<T> {

    Map<? extends Vertex<T>, ? extends HashSet<Edge<T>>> getAdjacencyList();

    Map<? extends Vertex<T>, ? extends HashSet<Edge<T>>> getTransposedAdjacencyList();

    void addVertex(Vertex<T> v);

    void addEdge(Edge<T> e);

    void removeVertex(Vertex<T> v);

    void removeEdge(Edge<T> e);

    boolean containsVertex(Vertex<T> v);

    boolean containsEdge(Edge<T> e);

}
