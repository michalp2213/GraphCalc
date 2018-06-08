package com.github.michalp2213.GraphCalc.Model;

import com.github.michalp2213.GraphCalc.Model.Graph.Edge;
import com.github.michalp2213.GraphCalc.Model.Graph.Vertex;
import com.github.michalp2213.GraphCalc.Model.Graphs.Poset;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosetTest {
    private Poset poset;

    @Before
    public void prepare() {
        poset = new Poset();
        poset.addVertex(new Vertex(1));
        poset.addVertex(new Vertex(2));
        poset.addEdge(new Edge(new Vertex(1), new Vertex(2)));
        poset.addEdge(new Edge(new Vertex(3), new Vertex(4)));
        poset.addEdge(new Edge(new Vertex(2), new Vertex(4)));
    }

    @Test
    public void addEdge() {
        poset.addEdge(new Edge(new Vertex(4), new Vertex(5)));
        try {
            poset.addEdge(new Edge(new Vertex(4), new Vertex(1)));
            fail("No exception!");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
        try {
            poset.addEdge(null);
            fail("No exception!");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
    }

    @Test
    public void containsVertex() {
        assertFalse(poset.containsVertex(new Vertex(128)));
        assertTrue(poset.containsVertex(new Vertex(1)));
        assertTrue(poset.containsVertex(new Vertex(2)));
        assertTrue(poset.containsVertex(new Vertex(3)));
        assertTrue(poset.containsVertex(new Vertex(4)));
    }

    @Test
    public void containsEdge() {
        assertTrue(poset.containsEdge(new Edge(new Vertex(1), new Vertex(2))));
        assertTrue(poset.containsEdge(new Edge(new Vertex(1), new Vertex(4))));
        assertFalse(poset.containsEdge(new Edge(new Vertex(4), new Vertex(1))));
        assertFalse(poset.containsEdge(new Edge(new Vertex(1), new Vertex(3))));
        assertFalse(poset.containsEdge(new Edge(new Vertex(12), new Vertex(-55))));
    }

    @Test
    public void removeVertex() {
        poset.removeVertex(new Vertex(2));
        assertTrue(poset.containsVertex(new Vertex(1)));
        assertTrue(poset.containsVertex(new Vertex(4)));
        assertFalse(poset.containsEdge(new Edge(new Vertex(1), new Vertex(2))));
        assertFalse(poset.containsEdge(new Edge(new Vertex(1), new Vertex(4))));
    }

    @Test
    public void removeEdge() {
        poset.removeEdge(new Edge(new Vertex(2), new Vertex(4)));
        assertTrue(poset.containsVertex(new Vertex(4)));
        assertTrue(poset.containsVertex(new Vertex(2)));
        assertFalse(poset.containsEdge(new Edge(new Vertex(1), new Vertex(4))));
        assertTrue(poset.containsEdge(new Edge(new Vertex(1), new Vertex(2))));
    }
}