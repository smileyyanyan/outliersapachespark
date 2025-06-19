package com.sanutty.clustering.hierarchical;

public class Edge implements Comparable<Edge> {
	
	private Cluster source;
	private Cluster target;

	private double distance;

	public Cluster getSource() {
		return source;
	}

	public void setSource(Cluster source) {
		this.source = source;
	}

	public Cluster getTarget() {
		return target;
	}

	public void setTarget(Cluster target) {
		this.target = target;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Edge o) {
        return distance < o.distance?-1:distance > o.distance?1:0;
	}
	
	public String toString() {
		return source.getName() + "-->" + target.getName() + "=" + distance;
	}
	
}
