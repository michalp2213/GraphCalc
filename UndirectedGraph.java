package Model;


import java.util.ArrayList;
import java.util.HashMap;

/*
    Class describing undirected graph.
 */

public class UndirectedGraph<T> implements Graph<T> {
    private HashMap<Vertex<T>, ArrayList<Edge<T>>> list;

    public UndirectedGraph() { //Basic constructor.
        list = new HashMap<>();
    }

    public UndirectedGraph(UndirectedGraph<T> g) { //Copying constructor
        list = new HashMap<>(g.list);
    }

    @Override
    public void addVertex(Vertex<T> v) {
        list.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    public void addEdge(Edge<T> e) {
        list.putIfAbsent(e.getFrom(), new ArrayList<>());
        list.putIfAbsent(e.getTo(), new ArrayList<>());
        list.get(e.getFrom()).add(e);
        list.get(e.getTo()).add(e.transpose());
    }

    @Override
    public void removeVertex(Vertex<T> v) {
        ArrayList<Edge<T>> arr = list.remove(v);
        if (v == null) return;
        for (Edge<T> e : arr) {
            list.get(e.getTo()).remove(e.transpose());
        }
    }

    @Override
    public void removeEdge(Edge<T> e) {
        ArrayList<Edge<T>> arr = list.get(e.getFrom());
        if (arr == null) return;
        arr.remove(e);
        list.get(e.getTo()).remove(e.transpose());
    }

    @Override
    public boolean containsVertex(Vertex<T> v) {
        return list.containsKey(v);
    }

    @Override
    public boolean containsEdge(Edge<T> e) {
        ArrayList<Edge<T>> arr = list.get(e.getFrom());
        return arr != null && arr.contains(e);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UndirectedGraph)) return false;
        return ((UndirectedGraph) obj).list.equals(list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
