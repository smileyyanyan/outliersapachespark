package com.sanutty.clustering.hierarchical;

import java.util.List;

public class EuclideanDistance {

	public static double calculateDistance(List<Double> input1, List<Double> input2) {
		double sum = 0;
		int size = input1.size();
		
		for (int i=0; i<size; i++) {
			final double d = input1.get(i) - input2.get(i);
			sum += d * d;
		}
		return Math.sqrt(sum);
	}

}
