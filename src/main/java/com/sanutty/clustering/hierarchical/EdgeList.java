package com.sanutty.clustering.hierarchical;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class EdgeList {
	
	private PriorityQueue<Edge> edges = new PriorityQueue<>();
	private Map<String, Edge> edgeLookupMap = new HashMap<>();
	
	
	public void addEdge(Edge e) {
		edges.add(e);
		edgeLookupMap.put(e.getSource().getName() + e.getTarget().getName(), e);
		edgeLookupMap.put(e.getTarget().getName() + e.getSource().getName(), e);
	}
	
	public void removeEdge(Edge e) {
		edges.remove(e);
		edgeLookupMap.remove(e.getSource().getName()+e.getTarget().getName());
		edgeLookupMap.remove(e.getTarget().getName()+e.getSource().getName());
	}
	
	public Edge getEdge(Cluster c1, Cluster c2) {
		return edgeLookupMap.get(c1.getName()+c2.getName());
	}
	
	public Edge removeNextShortestEdge() {
		Edge e = edges.remove();
		
		edgeLookupMap.remove(e.getSource().getName()+e.getTarget().getName());
		edgeLookupMap.remove(e.getTarget().getName()+e.getSource().getName());
		
		return e;
	}
	
	public int size() {
		return edges.size();
	}
	
	public Collection<Edge> getAllEdges() {
		return edges;
	}
	
}
