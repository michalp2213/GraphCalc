package com.github.michalp2213.GraphCalc.Model.Graphs;

import com.github.michalp2213.GraphCalc.Model.Graph.Edge;
import com.github.michalp2213.GraphCalc.Model.Graph.Graph;
import com.github.michalp2213.GraphCalc.Model.Graph.Vertex;

import java.util.*;

/**
 * Class describing undirected graph. Null as vertex isn't accepted.
 */

public class UndirectedGraph implements Graph {
    private HashMap<Vertex, HashSet<Edge>> list;

    /**
     * Basic constructor.
     */

    public UndirectedGraph() {
        list = new HashMap<>();
    }

    /**
     * Copying constructor
     */

    public UndirectedGraph(UndirectedGraph g) {
        if (g == null) throw new NullPointerException();
        list = new HashMap<>(g.list);
    }

    /**
     * @return read-only adjacency list that represents graph.
     */

    public Map<Vertex, ? extends HashSet<Edge>> getAdjacencyList() {
        return Collections.unmodifiableMap(list);
    }

    public Map<Vertex, ? extends HashSet<Edge>> getTransposedAdjacencyList() {
        return Collections.unmodifiableMap(list);
    }

    @Override
    public void addVertex(Vertex v) {
        if (v == null) throw new NullPointerException();
        list.putIfAbsent(v, new HashSet<>());
    }

    @Override
    public void addEdge(Edge e) {
        if (e == null) throw new NullPointerException();
        list.putIfAbsent(e.from, new HashSet<>());
        list.putIfAbsent(e.to, new HashSet<>());
        list.get(e.from).add(e);
        list.get(e.to).add(e.transpose());
    }

    @Override
    public void removeVertex(Vertex v) {
        if (v == null) throw new NullPointerException();
        HashSet<Edge> set = list.remove(v);
        if (set == null) return;
        for (Edge e : set) {
            list.get(e.to).remove(e.transpose());
        }
    }

    @Override
    public void removeEdge(Edge e) {
        if (e == null) throw new NullPointerException();
        HashSet<Edge> set = list.get(e.from);
        if (set == null) return;
        set.remove(e);
        list.get(e.to).remove(e.transpose());
    }

    @Override
    public boolean containsVertex(Vertex v) {
        return list.containsKey(v);
    }

    @Override
    public boolean containsEdge(Edge e) {
        HashSet<Edge> set = list.get(e.from);
        return set != null && set.contains(e);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof UndirectedGraph) && ((UndirectedGraph) obj).list.equals(list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
