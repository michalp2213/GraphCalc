package com.github.michalp2213.GraphCalc.Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UndirectedGraphTest {

    UndirectedGraph<Integer> graph;

    @Before
    public void prepare() {
        graph = new UndirectedGraph<>();
        graph.addVertex(new Vertex<>(1));
        graph.addVertex(new Vertex<>(2));
        graph.addEdge(new Edge<>(new Vertex<>(1), new Vertex<>(2)));
        graph.addEdge(new Edge<>(new Vertex<>(3), new Vertex<>(4)));
        graph.addEdge(new Edge<>(new Vertex<>(2), new Vertex<>(4)));
    }

    @Test
    public void containsVertex() {
        assertTrue(graph.containsVertex(new Vertex<>(1)));
        assertTrue(graph.containsVertex(new Vertex<>(2)));
        assertTrue(graph.containsVertex(new Vertex<>(3)));
        assertTrue(graph.containsVertex(new Vertex<>(4)));
        assertFalse(graph.containsVertex(new Vertex<>(128)));
    }

    @Test
    public void containsEdge() {
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(1), new Vertex<>(2))));
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(2), new Vertex<>(1))));
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(2), new Vertex<>(4))));
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(4), new Vertex<>(3))));
        assertFalse(graph.containsEdge(new Edge<>(new Vertex<>(1), new Vertex<>(4))));
    }

    @Test
    public void removeVertex() {
        graph.removeVertex(new Vertex<>(2));
        assertFalse(graph.containsEdge(new Edge<>(new Vertex<>(1), new Vertex<>(2))));
        assertFalse(graph.containsEdge(new Edge<>(new Vertex<>(2), new Vertex<>(1))));
        assertFalse(graph.containsEdge(new Edge<>(new Vertex<>(2), new Vertex<>(4))));
        assertTrue(graph.containsVertex(new Vertex<>(1)));
        assertFalse(graph.containsVertex(new Vertex<>(2)));
        assertTrue(graph.containsVertex(new Vertex<>(3)));
        assertTrue(graph.containsVertex(new Vertex<>(4)));
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(3), new Vertex<>(4))));
    }

    @Test
    public void removeEdge() {
        graph.removeEdge(new Edge<>(new Vertex<>(2), new Vertex<>(4)));
        assertFalse(graph.containsEdge(new Edge<>(new Vertex<>(2), new Vertex<>(4))));
        assertFalse(graph.containsEdge(new Edge<>(new Vertex<>(4), new Vertex<>(2))));
        assertTrue(graph.containsVertex(new Vertex<>(2)));
        assertTrue(graph.containsVertex(new Vertex<>(4)));
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(3), new Vertex<>(4))));
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(4), new Vertex<>(3))));
        assertTrue(graph.containsEdge(new Edge<>(new Vertex<>(1), new Vertex<>(2))));
    }

}