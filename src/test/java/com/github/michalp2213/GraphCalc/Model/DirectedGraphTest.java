package com.github.michalp2213.GraphCalc.Model;

import com.github.michalp2213.GraphCalc.Model.Graph.Edge;
import com.github.michalp2213.GraphCalc.Model.Graph.Vertex;
import com.github.michalp2213.GraphCalc.Model.Graphs.DirectedGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DirectedGraphTest {
    private DirectedGraph graph;

    @Before
    public void prepare() {
        graph = new DirectedGraph();
        graph.addVertex(new Vertex(11));
        graph.addVertex(new Vertex(22));
        graph.addEdge(new Edge(new Vertex(11), new Vertex(22)));
        graph.addEdge(new Edge(new Vertex(33), new Vertex(44)));
        graph.addEdge(new Edge(new Vertex(22), new Vertex(44)));
    }

    @Test
    public void containsVertex() {
        assertTrue(graph.containsVertex(new Vertex(11)));
        assertTrue(graph.containsVertex(new Vertex(22)));
        assertTrue(graph.containsVertex(new Vertex(33)));
        assertTrue(graph.containsVertex(new Vertex(44)));
        assertFalse(graph.containsVertex(new Vertex(128)));
    }

    @Test
    public void containsEdge() {
        assertTrue(graph.containsEdge(new Edge(new Vertex(11), new Vertex(22))));
        assertFalse(graph.containsEdge(new Edge(new Vertex(22), new Vertex(11))));
        assertTrue(graph.containsEdge(new Edge(new Vertex(22), new Vertex(44))));
        assertFalse(graph.containsEdge(new Edge(new Vertex(44), new Vertex(33))));
        assertFalse(graph.containsEdge(new Edge(new Vertex(1), new Vertex(4))));
    }

    @Test
    public void removeVertex() {
        graph.removeVertex(new Vertex(22));
        assertFalse(graph.containsEdge(new Edge(new Vertex(11), new Vertex(22))));
        assertFalse(graph.containsEdge(new Edge(new Vertex(22), new Vertex(44))));
        assertTrue(graph.containsVertex(new Vertex(11)));
        assertFalse(graph.containsVertex(new Vertex(22)));
        assertTrue(graph.containsVertex(new Vertex(33)));
        assertTrue(graph.containsVertex(new Vertex(44)));
        assertTrue(graph.containsEdge(new Edge(new Vertex(33), new Vertex(44))));
    }

    @Test
    public void removeEdge() {
        graph.removeEdge(new Edge(new Vertex(22), new Vertex(44)));
        assertFalse(graph.containsEdge(new Edge(new Vertex(22), new Vertex(44))));
        assertTrue(graph.containsVertex(new Vertex(22)));
        assertTrue(graph.containsVertex(new Vertex(44)));
        assertTrue(graph.containsEdge(new Edge(new Vertex(33), new Vertex(44))));
        assertFalse(graph.containsEdge(new Edge(new Vertex(44), new Vertex(33))));
        assertTrue(graph.containsEdge(new Edge(new Vertex(11), new Vertex(22))));
    }
}