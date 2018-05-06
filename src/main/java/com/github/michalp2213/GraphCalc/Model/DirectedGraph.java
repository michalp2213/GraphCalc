package com.github.michalp2213.GraphCalc.Model;

import java.util.*;

/**
 * Class describing directed graph. Null as vertex isn't accepted.
 */

public class DirectedGraph<T> implements Graph<T> {
    protected HashMap<Vertex<T>, HashSet<Edge<T>>> list;
    protected HashMap<Vertex<T>, HashSet<Edge<T>>> transposedList;

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

    public DirectedGraph(DirectedGraph<T> g) {
        if (g == null) throw new NullPointerException();
        list = new HashMap<>(g.list);
        transposedList = new HashMap<>(g.transposedList);
    }

    /**
     * Get read-only adjacency list that represents graph.
     */

    @Override
    public Map<? extends Vertex<T>, ? extends HashSet<Edge<T>>> getAdjacencyList() {
        return Collections.unmodifiableMap(list);
    }

    /**
     * Get read-only transposed adjacency list that represents graph.
     */

    public Map<? extends Vertex<T>, ? extends HashSet<Edge<T>>> getTransposedAdjacencyList() {
        return Collections.unmodifiableMap(transposedList);
    }

    /**
     * Return new graph that has transposed edges.
     */

    public DirectedGraph<T> transpose() {
        DirectedGraph<T> g = new DirectedGraph<>(this);
        HashMap<Vertex<T>, HashSet<Edge<T>>> temp = g.list;
        g.list = g.transposedList;
        g.transposedList = temp;
        return g;
    }

    @Override
    public void addVertex(Vertex<T> v) {
        if (v == null) throw new NullPointerException();
        list.putIfAbsent(v, new HashSet<>());
        transposedList.putIfAbsent(v, new HashSet<>());
    }

    @Override
    public void addEdge(Edge<T> e) {
        if (e == null) throw new NullPointerException();
        list.putIfAbsent(e.from, new HashSet<>());
        list.putIfAbsent(e.to, new HashSet<>());
        transposedList.putIfAbsent(e.from, new HashSet<>());
        transposedList.putIfAbsent(e.to, new HashSet<>());
        list.get(e.from).add(e);
        transposedList.get(e.to).add(e.transpose());
    }

    @Override
    public void removeVertex(Vertex<T> v) {
        if (v == null) throw new NullPointerException();
        for (Edge<T> e : transposedList.get(v)) {
            e.finishIt();
            list.get(e.to).remove(e.transpose());
        }
        for (Edge<T> e : list.get(v)) {
            e.finishIt();
            transposedList.get(e.to).remove(e.transpose());
        }
        v.finishIt();
        list.remove(v);
        transposedList.remove(v);
    }

    @Override
    public void removeEdge(Edge<T> e) {
        if (e == null) throw new NullPointerException();
        HashSet<Edge<T>> set = list.get(e.from);
        if (set == null) return;
        e.finishIt();
        set.remove(e);
        set = transposedList.get(e.to);
        if (set != null) set.remove(e.transpose());
    }

    @Override
    public boolean containsVertex(Vertex<T> v) {
        return list.containsKey(v);
    }

    @Override
    public boolean containsEdge(Edge<T> e) {
        HashSet<Edge<T>> set = list.get(e.from);
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
