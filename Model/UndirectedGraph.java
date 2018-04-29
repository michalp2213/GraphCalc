package Model;


import java.util.ArrayList;
import java.util.HashMap;

/*
 * Class describing undirected graph.
 */

public class UndirectedGraph<T> implements Graph<T> {
    private HashMap<Vertex<T>, ArrayList<Edge<T>>> list;

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
        list.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    public void addEdge(Edge<T> e) {
        list.putIfAbsent(e.from, new ArrayList<>());
        list.putIfAbsent(e.to, new ArrayList<>());
        list.get(e.from).add(e);
        list.get(e.to).add(e.transpose());
    }

    @Override
    public void removeVertex(Vertex<T> v) {
        ArrayList<Edge<T>> arr = list.remove(v);
        if (v == null) return;
        for (Edge<T> e : arr) {
            list.get(e.to).remove(e.transpose());
        }
    }

    @Override
    public void removeEdge(Edge<T> e) {
        ArrayList<Edge<T>> arr = list.get(e.from);
        if (arr == null) return;
        arr.remove(e);
        list.get(e.to).remove(e.transpose());
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
        return (obj instanceof UndirectedGraph) && ((UndirectedGraph) obj).list.equals(list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
