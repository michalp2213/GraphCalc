package com.github.michalp2213.GraphCalc.Model;

import com.sun.istack.internal.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/*
 * Class describing directed graph. Null as vertex isn't accepted.
 */

public class DirectedGraph<T> implements Graph<T> {
    protected HashMap<Vertex<T>, HashSet<Edge<T>>> list;

    /*
     * Basic constructor.
     */

    public DirectedGraph() {
        list = new HashMap<>();
    }

    /*
     * Copying constructor.
     */

    public DirectedGraph(DirectedGraph<T> g) {
        if (g == null) throw new NullPointerException();
        list = new HashMap<>(g.list);
    }

    /*
     * Get read-only adjacency list that represents graph.
     */

    public Map<? extends Vertex<T>, ? extends HashSet<Edge<T>>> getAdjacencyList() {
        return Collections.unmodifiableMap(list);
    }

    @Override
    @NotNull
    public void addVertex(Vertex<T> v) {
        if (v == null) throw new NullPointerException();
        list.putIfAbsent(v, new HashSet<>());
    }

    @Override
    @NotNull
    public void addEdge(Edge<T> e) {
        if (e == null) throw new NullPointerException();
        list.putIfAbsent(e.from, new HashSet<>());
        list.putIfAbsent(e.to, new HashSet<>());
        list.get(e.from).add(e);
    }

    @Override
    @NotNull
    public void removeVertex(Vertex<T> v) {
        if (v == null) throw new NullPointerException();
        list.remove(v);
        for (HashSet<Edge<T>> set : list.values()) {
            set.removeIf(e -> e.to.equals(v));
        }
    }

    @Override
    @NotNull
    public void removeEdge(Edge<T> e) {
        if (e == null) throw new NullPointerException();
        HashSet<Edge<T>> set = list.get(e.from);
        if (set == null) return;
        set.remove(e);
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
