package com.github.michalp2213.GraphCalc.Model.Graph;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;

/**
 * Graph interface describing basic operations on graph.
 */


public interface Graph extends Serializable {

    Map<Vertex, ? extends HashSet<Edge>> getAdjacencyList();

    Map<Vertex, ? extends HashSet<Edge>>  getTransposedAdjacencyList();

    void addVertex(Vertex v);

    void addEdge(Edge e);

    void removeVertex(Vertex v);

    void removeEdge(Edge e);

    boolean containsVertex(Vertex v);

    boolean containsEdge(Edge e);
}
