package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
 * Class describing directed graph.
 */

public class DirectedGraph<T> implements Graph<T> {
    private HashMap<Vertex<T>, ArrayList<Edge<T>>> list;

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
        list = new HashMap<>(g.list);
    }

    @Override
    public void addVertex(Vertex<T> v) {
        list.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    public void addEdge(Edge<T> e) {
        list.putIfAbsent(e.from, new ArrayList<>());
        list.putIfAbsent(e.to, new ArrayList<>());
        list.get(e.from).add(e);
    }

    @Override
    public void removeVertex(Vertex<T> v) {
        list.remove(v);
        for (ArrayList<Edge<T>> arr : list.values()) {
            Iterator<Edge<T>> it = arr.iterator();
            while (it.hasNext()) {
                Edge<T> e = it.next();
                if (e.to.equals(v)) it.remove();
                ;
            }
        }
    }

    @Override
    public void removeEdge(Edge<T> e) {
        ArrayList<Edge<T>> arr = list.get(e.from);
        if (arr == null) return;
        arr.remove(e);
    }

    @Override
    public boolean containsVertex(Vertex<T> v) {
        return list.containsKey(v);
    }

    @Override
    public boolean containsEdge(Edge<T> e) {
        ArrayList<Edge<T>> arr = list.get(e.from);
        return arr != null && arr.contains(e);
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
