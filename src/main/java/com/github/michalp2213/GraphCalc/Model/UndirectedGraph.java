package com.github.michalp2213.GraphCalc.Model;

import java.util.*;

/**
 * Class describing undirected graph. Null as vertex isn't accepted.
 */

public class UndirectedGraph<T> implements Graph<T> {
	static final long serialVersionUID = 01L;

    protected HashMap<Vertex<T>, HashSet<Edge<T>>> list;

    /**
     * Basic constructor.
     */

    public UndirectedGraph() {
        list = new HashMap<>();
    }

    /**
     * Copying constructor
     */

    public UndirectedGraph(UndirectedGraph<T> g) {
        if (g == null) throw new NullPointerException();
        list = new HashMap<>(g.list);
    }

    /**
     * Get read-only adjacency list that represents graph.
     */

    public Map<? extends Vertex<T>, ? extends HashSet<Edge<T>>> getAdjacencyList() {
        return Collections.unmodifiableMap(list);
    }

    @Override
    public void addVertex(Vertex<T> v) {
        if (v == null) throw new NullPointerException();
        list.putIfAbsent(v, new HashSet<>());
    }

    @Override
    public void addEdge(Edge<T> e) {
        if (e == null) throw new NullPointerException();
        list.putIfAbsent(e.from, new HashSet<>());
        list.putIfAbsent(e.to, new HashSet<>());
        list.get(e.from).add(e);
        list.get(e.to).add(e.transpose());
    }

    @Override
    public void removeVertex(Vertex<T> v) {
        if (v == null) throw new NullPointerException();
        v.finishIt();
        HashSet<Edge<T>> set = list.remove(v);
        if (set == null) return;
        for (Edge<T> e : set) {
            e.finishIt();
            e.transpose().finishIt();
            list.get(e.to).remove(e.transpose());
        }
    }

    @Override
    public void removeEdge(Edge<T> e) {
        if (e == null) throw new NullPointerException();
        HashSet<Edge<T>> set = list.get(e.from);
        if (set == null) return;
        e.finishIt();
        set.remove(e);
        e.transpose().finishIt();
        list.get(e.to).remove(e.transpose());
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
        return (obj instanceof UndirectedGraph) && ((UndirectedGraph) obj).list.equals(list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
