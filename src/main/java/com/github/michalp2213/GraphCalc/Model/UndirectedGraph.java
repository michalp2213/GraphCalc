package com.github.michalp2213.GraphCalc.Model;

import java.util.HashMap;
import java.util.HashSet;

/*
 * Class describing undirected graph.
 */

public class UndirectedGraph<T> implements Graph<T> {
    public HashMap<Vertex<T>, HashSet<Edge<T>>> list;

    /*
     * Basic constructor.
     */

    public UndirectedGraph() {
        list = new HashMap<>();
    }

    /*
     * Copying constructor
     */

    public UndirectedGraph(UndirectedGraph<T> g) {
        list = new HashMap<>(g.list);
    }

    @Override
    public void addVertex(Vertex<T> v) {
        list.putIfAbsent(v, new HashSet<>());
    }

    @Override
    public void addEdge(Edge<T> e) {
        list.putIfAbsent(e.from, new HashSet<>());
        list.putIfAbsent(e.to, new HashSet<>());
        list.get(e.from).add(e);
        list.get(e.to).add(e.transpose());
    }

    @Override
    public void removeVertex(Vertex<T> v) {
        HashSet<Edge<T>> set = list.remove(v);
        if (v == null) return;
        for (Edge<T> e : set) {
            list.get(e.to).remove(e.transpose());
        }
    }

    @Override
    public void removeEdge(Edge<T> e) {
        HashSet<Edge<T>> set = list.get(e.from);
        if (set == null) return;
        set.remove(e);
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
