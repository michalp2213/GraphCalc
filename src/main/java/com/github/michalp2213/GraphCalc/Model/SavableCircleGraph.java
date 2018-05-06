package com.github.michalp2213.GraphCalc.Model;

import java.util.HashSet;
import java.util.Map;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class SavableCircleGraph implements Graph<Circle> {
	private Graph<Circle> drawable;
	private Graph<SerializableCircle> savable;
	
	public SavableCircleGraph (Type t) {
		switch (t) {
			case DIRECTED:
				drawable = new DirectedGraph<>();
				savable = new DirectedGraph<>();
				break;
			case UNDIRECTED:
				drawable = new UndirectedGraph<>();
				savable = new UndirectedGraph<>();
				break;
			case POSET:
				drawable = new Poset<>();
				savable = new Poset<>();
		}
	}
	
	@Override
	public void addVertex(Vertex<Circle> v) {
		drawable.addVertex(v);
		savable.addVertex(new SerializableCircleVertex(v));
	}
	
	public void addVertex(CircleVertex v) {
		v.draw();
		
		drawable.addVertex(v);
		savable.addVertex(new SerializableCircleVertex(v));
	}

	@Override
	public void addEdge(Edge<Circle> e) {
		drawable.addEdge(e);
		savable.addEdge(new Edge<>(new SerializableCircleVertex(e.from), new SerializableCircleVertex(e.to)));
	}
	
	public void addEdge(LineEdge e) {
		e.draw();
		
		drawable.addEdge(e);
		savable.addEdge(new Edge<>(new SerializableCircleVertex(e.from), new SerializableCircleVertex(e.to)));
	}

	@Override
	public boolean containsVertex(Vertex<Circle> v) {
		return drawable.containsVertex(v);
	}
	
	@Override
	public boolean containsEdge(Edge<Circle> e) {
		return drawable.containsEdge(e);
	}
	
	@Override
	public void removeVertex(Vertex<Circle> v) {
		drawable.removeVertex(v);
		savable.removeVertex(new SerializableCircleVertex(v));
	}
	
	@Override
	public void removeEdge(Edge<Circle> e) {
		drawable.removeEdge(e);
		savable.removeEdge(new Edge<>(new SerializableCircleVertex(e.from), new SerializableCircleVertex(e.to)));
	}
	
	@Override
	public Map<? extends Vertex<Circle>, ? extends HashSet<Edge<Circle>>> getAdjacencyList() {
		return drawable.getAdjacencyList();
	}
	
	public Graph<SerializableCircle> getSerializable() {
		return savable;
	}

	public static enum Type {
		DIRECTED, UNDIRECTED, POSET;
	}
}
