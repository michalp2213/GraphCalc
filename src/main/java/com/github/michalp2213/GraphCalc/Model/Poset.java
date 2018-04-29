package com.github.michalp2213.GraphCalc.Model;

import java.util.HashMap;

/*
 * Class describing poset(partially ordered set).
 */

public class Poset<T> extends DirectedGraph<T> {

    /*
     * Basic constructor.
     */

    public Poset() {
        super();
    }

    /*
     * Copying constructor.
     */

    public Poset(Poset<T> p) {
        super(p);
    }

    /*
     * Edge must not ruin antisymmetry.
     */

    @Override
    public void addEdge(Edge<T> e) {
        if (e.from.equals(e.to) || containsEdge(e)) return;
        if (containsEdge(e.transpose())) throw new IllegalArgumentException();
        addVertex(e.from);
        addVertex(e.to);
        list.get(e.from).add(e);
    }

    /*
     * Also checks transitive edges.
     */

    @Override
    public boolean containsEdge(Edge<T> e) {
        HashMap<Vertex<T>, Boolean> visited = new HashMap<>();
        return dfs(e.from, e.to, visited);
    }

    private boolean dfs(Vertex<T> v, Vertex<T> to, HashMap<Vertex<T>, Boolean> visited) {
        visited.put(v, true);
        for (Edge<T> e : list.get(v)) {
            if (e.to.equals(to)) return true;
            if (!visited.containsKey(e.to)) {
                boolean res = dfs(e.to, to, visited);
                if (res) return true;
            }
        }
        return false;
    }
}
