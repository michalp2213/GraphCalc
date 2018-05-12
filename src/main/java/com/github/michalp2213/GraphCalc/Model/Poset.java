package com.github.michalp2213.GraphCalc.Model;

import java.util.HashMap;
import java.util.Set;

/**
 * Class describing poset(partially ordered set). Null as vertex isn't accepted.
 */

public class Poset extends DirectedGraph {

    /**
     * Basic constructor.
     */

    public Poset() {
        super();
    }

    /**
     * Copying constructor.
     */

    public Poset(Poset p) {
        super(p);
    }

    /**
     * Edge must not ruin antisymmetry.
     */

    @Override
    public void addEdge(Edge e) {
        if (e == null) throw new NullPointerException();
        if (e.from.equals(e.to) || containsEdge(e)) return;
        if (containsEdge(e.transpose())) throw new IllegalArgumentException();
        addVertex(e.from);
        addVertex(e.to);
        list.get(e.from).add(e);
        transposedList.get(e.to).add(e.transpose());
    }

    /**
     * Removes only direct edges between vertices.
     */

    @Override
    public void removeEdge(Edge e) {
        super.removeEdge(e);
    }

    /**
     * Also checks transitive edges.
     */

    @Override
    public boolean containsEdge(Edge e) {
        if (e == null) throw new NullPointerException();
        if (!containsVertex(e.from) || !containsVertex(e.to)) return false;
        HashMap<Vertex, Boolean> visited = new HashMap<>();
        return dfs(e.from, e.to, visited);
    }

    private boolean dfs(Vertex v, Vertex to, HashMap<Vertex, Boolean> visited) {
        visited.put(v, true);
        for (Edge e : list.get(v)) {
            if (e.to.equals(to)) return true;
            if (!visited.containsKey(e.to)) {
                boolean res = dfs(e.to, to, visited);
                if (res) return true;
            }
        }
        return false;
    }
}
