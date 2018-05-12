package com.github.michalp2213.GraphCalc.Model.Algorithms;

import com.github.michalp2213.GraphCalc.Model.AlgorithmEvents.AlgorithmEvent;
import com.github.michalp2213.GraphCalc.Model.AlgorithmEvents.TouchEvent;
import com.github.michalp2213.GraphCalc.Model.AlgorithmEvents.VisitEvent;
import com.github.michalp2213.GraphCalc.Model.Edge;
import com.github.michalp2213.GraphCalc.Model.Graph;
import com.github.michalp2213.GraphCalc.Model.Vertex;

import java.util.*;

public class DFS extends GraphAlgorithm{
    private static HashSet<Vertex> visited = new HashSet<>();

    public static ArrayList<AlgorithmEvent> run(Graph g, Vertex startingVertex){
        clearEvents();
        runDFS(g, startingVertex);
        return getEvents();
    }

    private static void runDFS(Graph g, Vertex curr){
        addEvent(new VisitEvent(curr));
        for (Edge e : g.getAdjacencyList().get(curr)){
            addEvent(new TouchEvent(e));
            if (!visited.contains(e.to)){
                visited.add(e.to);
                runDFS(g, e.to);
            }
        }
    }
}
