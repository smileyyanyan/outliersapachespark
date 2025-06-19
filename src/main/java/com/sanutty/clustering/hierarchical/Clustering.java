package com.sanutty.clustering.hierarchical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Clustering {

	private EdgeList edgeList = new EdgeList();
	private EdgeList compositeEdgeList = new EdgeList();
	private EdgeList toKeepEdgeList = new EdgeList();
	private double distanceThreshold = 0.2;
	
	private int globalIndex = 1;
	
	public Map<Integer, Set<Cluster>> cluster(List<Cluster> initialData) {
		
		int size = initialData.size();
		List<Cluster> clusters = new ArrayList<>();
		clusters.addAll(initialData);
		
		//-------------------------------------------------------
		// Calculate pairwise distance 
		// create an edge for each pair
		//-------------------------------------------------------
		for (int i=0; i<size-1; i++) {
			for (int j=i+1; j<size; j++) {
				Cluster c1 = initialData.get(i);
				Cluster c2 = initialData.get(j);
				List<Double> f1 = c1.getFeatureVector();
				List<Double> f2 = c2.getFeatureVector();
				double distance = EuclideanDistance.calculateDistance(f1, f2);
				Edge e = new Edge();
				e.setSource(c1);
				e.setTarget(c2);
				e.setDistance(distance);				
				edgeList.addEdge(e);
			}
		}
		
		System.out.println(edgeList.getAllEdges());
		
		//-------------------------------------------------------
		// Remove edges one at a time 
		// Start forming new cluster with the clusters from removed edge
		//-------------------------------------------------------
		while (clusters.size() > 1) {
			Edge e = edgeList.removeNextShortestEdge();
			Cluster removedSource = e.getSource();
			Cluster removedTarget = e.getTarget();
			
			//form a new cluster composite1
			Cluster compositeCluster = new Cluster("composite" + globalIndex++);
			compositeCluster.setLeaf(false);
			compositeCluster.setLeftCluster(removedSource);
			compositeCluster.setRightCluster(removedTarget);
			
			// Remove the children from remaining clusters
			
			clusters.remove(removedSource);  //c1
			clusters.remove(removedTarget);  //c2 
			
			// composite1 (c1, c2)
			
			// Now create a new edge between composite1 and each remaining cluster
			// Example: next cluster is c3
			//    find edge c1-->c3 dist = 0.5
			//    find edge c2-->c3 dist = 0.25
			//	  Use a link stragety to get distance, e.g. avg (0.5+0.25)/2
			//	  Now create new edge composite1 --> c3 distance = (0.5+0.25)/2
			
			// create edge from new cluster to each existing cluster
			for (Cluster cluster : clusters) {
				// find edge cluster -> removed source 
				Edge edgeBetweenClusterAndRemovedSourceCluster = edgeList.getEdge(cluster, removedSource);
				
				// find edge cluster -> removed target 
				Edge edgeBetweenClusterAndRemovedTargetCluster = edgeList.getEdge(cluster, removedTarget);
				
				if (edgeBetweenClusterAndRemovedSourceCluster!=null && edgeBetweenClusterAndRemovedTargetCluster!=null) {
					// calculate link strategy
					double avgdistance = (edgeBetweenClusterAndRemovedSourceCluster.getDistance() + edgeBetweenClusterAndRemovedTargetCluster.getDistance()) /2.0;
					edgeList.removeEdge(edgeBetweenClusterAndRemovedSourceCluster);
					edgeList.removeEdge(edgeBetweenClusterAndRemovedTargetCluster);
					
					// create new edge between compositeCluster -> cluster, dist = avg
					Edge compositeEdge = new Edge();
					// combo1 --> c3 
					compositeEdge.setSource(cluster);
					compositeEdge.setTarget(compositeCluster);
					compositeEdge.setDistance(avgdistance);
					edgeList.addEdge(compositeEdge);
					compositeEdgeList.addEdge(compositeEdge);
					
				}
			}
			clusters.add(compositeCluster);
			System.out.println("Composite edges: " + compositeEdgeList.getAllEdges());
			System.out.println("Clusters: " + clusters);
		}  //end while
		
		System.out.println(clusters);
		
		/*

          composite9
         /         \
		c4	   composite8
			  /           \ 
        composite2         composite7
        /   \             /          \
       c5   c9  composite4            composite6 
               /       \             /          \
              c3   composite1   composite3   composite5
                   /     \       /    \       /       \
                  c1     c2    c8    c10     c6       c7

		
		--------------------
		After pruning
		--------------------
		
		c3-->composite1=0.0, 
		composite3-->composite5=0.035971243461552
		composite4-->composite6=0.14351098953556973 
		
		--------------------
		Edges removed
		--------------------
		c4 --> composite8
		composite2 --> composite7
		c3 --> composite1		
		
		 */
		
		//-------------------------------------------------------
		// For each cluster, use distance threshold to 
		// keep qualified edges
		//-------------------------------------------------------
		pruneEdge(clusters.get(0));
		
		Map<Integer, Set<Cluster>> clusterMap = new HashMap<>();
		int clusterId = 1;
		Set<Cluster> visited = new HashSet<>();
		
		//-------------------------------------------------------
		// The selected to keep edges are the clusters 
		// Now we just need to make sure we are not counting
		// composite clusters:
		//
		//  composite1 -> c1 c2 c3
		//  composite2 -> c4 c5 c6
		//  composite3 -> composite1 composite2
		//
		//	Keep: composite1 composite2
		//	eliminate: composite3
		//-------------------------------------------------------
		while (toKeepEdgeList.size()>0) {
			Edge e = toKeepEdgeList.removeNextShortestEdge();
			Set<Cluster> aClusterList = new HashSet<>();
			addLeafsToCluster(aClusterList, e.getSource());
			addLeafsToCluster(aClusterList, e.getTarget());	
			aClusterList.removeAll(visited);
			if (aClusterList.size() > 0) {
			  clusterMap.put(clusterId++, aClusterList);
			}
			visited.addAll(aClusterList);
		}
		
		System.out.println(clusterMap);
		
		return clusterMap;
		
	}
	
	/*
	 * If the edge distance is < distance threshold, keep it
	 * Otherwise, remove the edge. 
	 */
	private void pruneEdge(Cluster cluster) {
		Cluster leftCluster = cluster.getLeftCluster();
		Cluster rightCluster = cluster.getRightCluster();
		
		Edge edge = compositeEdgeList.getEdge(leftCluster, rightCluster);
		if (edge!=null && edge.getDistance() < distanceThreshold) {
			toKeepEdgeList.addEdge(edge);
		}
		
		if (!leftCluster.isLeaf()) {
			pruneEdge(leftCluster);
		}
		
		if (!rightCluster.isLeaf()) {
			pruneEdge(rightCluster);
		}
	}
	
	private void addLeafsToCluster(Set<Cluster> aClusterList, Cluster cluster) {
		if (cluster.isLeaf()) {
			aClusterList.add(cluster);
		} else {
			addLeafsToCluster(aClusterList, cluster.getLeftCluster());
			addLeafsToCluster(aClusterList, cluster.getRightCluster());
		}
	}
}
