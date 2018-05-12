package com.github.michalp2213.GraphCalc.Model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Class describing directed graph.
 */

public class DirectedGraph implements Graph {
    HashMap<Vertex, HashSet<Edge>> list;
    HashMap<Vertex, HashSet<Edge>> transposedList;

    /**
     * Basic constructor.
     */

    public DirectedGraph() {
        list = new HashMap<>();
        transposedList = new HashMap<>();
    }

    /**
     * Copying constructor.
     */

    public DirectedGraph(DirectedGraph g) {
        if (g == null) throw new NullPointerException();
        list = new HashMap<>(g.list);
        transposedList = new HashMap<>(g.transposedList);
    }

    /**
     * @return read-only adjacency list that represents graph.
     */

    @Override
    public Map<Vertex, ? extends HashSet<Edge>> getAdjacencyList() {
        return Collections.unmodifiableMap(list);
    }

    /**
     * @return read-only transposed adjacency list that represents graph.
     */

    public Map<Vertex, ? extends HashSet<Edge>> getTransposedAdjacencyList() {
        return Collections.unmodifiableMap(transposedList);
    }

    /**
     * @return graph that has transposed edges.
     */

    public DirectedGraph transpose() {
        DirectedGraph g = new DirectedGraph(this);
        HashMap<Vertex, HashSet<Edge>> temp = g.list;
        g.list = g.transposedList;
        g.transposedList = temp;
        return g;
    }

    @Override
    public void addVertex(Vertex v) {
        if (v == null) throw new NullPointerException();
        list.putIfAbsent(v, new HashSet<>());
        transposedList.putIfAbsent(v, new HashSet<>());
    }

    @Override
    public void addEdge(Edge e) {
        if (e == null) throw new NullPointerException();
        list.putIfAbsent(e.from, new HashSet<>());
        list.putIfAbsent(e.to, new HashSet<>());
        transposedList.putIfAbsent(e.from, new HashSet<>());
        transposedList.putIfAbsent(e.to, new HashSet<>());
        list.get(e.from).add(e);
        transposedList.get(e.to).add(e.transpose());
    }

    @Override
    public void removeVertex(Vertex v) {
        if (v == null) throw new NullPointerException();
        if (transposedList.get(v) != null) {
            for (Edge e : transposedList.get(v)) {
                list.get(e.to).remove(e.transpose());
            }
        }
        if (list.get(v) != null) {
            for (Edge e : list.get(v)) {
                transposedList.get(e.to).remove(e.transpose());
            }
        }
        list.remove(v);
        transposedList.remove(v);
    }

    @Override
    public void removeEdge(Edge e) {
        if (e == null) throw new NullPointerException();
        HashSet<Edge> set = list.get(e.from);
        if (set == null) return;
        set.remove(e);
        set = transposedList.get(e.to);
        if (set != null) set.remove(e.transpose());
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
        return (obj instanceof DirectedGraph) && ((DirectedGraph) obj).list.equals(list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
