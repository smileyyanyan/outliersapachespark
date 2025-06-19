package com.sanutty.clustering.hierarchical;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

	private String name;	
	private boolean isLeaf = true;
	
	private Cluster left;
	private Cluster right;
	
	private List<Double> featureVector = new ArrayList<>();
	
	public Cluster(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}
	
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public Cluster getLeftCluster() {
		return left;
	}
	
	public void setLeftCluster(Cluster left) {
		this.left = left;
	}
	
	public Cluster getRightCluster() {
		return right;
	}
	
	public void setRightCluster(Cluster right) {
		this.right = right;
	}
	
	public List<Cluster> getChildren() {
		return null;
	}

	public List<Double> getFeatureVector() {
		return featureVector;
	}

	public void setFeatureVector(List<Double> featureVector) {
		this.featureVector = featureVector;
	}
	
	@Override
	public String toString() {
		if (isLeaf) {
			return name;
		} else {
			return name + "->("+left.getName()+"-"+right.getName()+")";
		}
	}
}
