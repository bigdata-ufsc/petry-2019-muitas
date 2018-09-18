package br.com.lucasmaypetry.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.lucasmaypetry.distance.DistanceFunction;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;
import lombok.Getter;

public class Application {

	@Getter
	private List<String> features;

	private Map<String, Double> weights;
	private Map<String, Double> thresholds;
	private Map<String, DistanceFunction<Feature>> distanceFunctions;

	public Application() {
		this.features = new ArrayList<>();
		this.weights = new HashMap<>();
		this.thresholds = new HashMap<>();
		this.distanceFunctions = new HashMap<>();
	}

	public Application(String... features) {
		this.features = Arrays.asList(features);
		this.weights = new HashMap<>();
		this.thresholds = new HashMap<>();
	}

	public void clear() {
		this.features.clear();
		this.weights.clear();
		this.thresholds.clear();
	}

	public Application setWeight(String feature, double weight) {
		this.weights.put(feature, weight);
		return this;
	}

	public double getWeight(String feature) {
		try {
			return this.weights.get(feature);
		} catch (Exception e) {
			Logger.log(Type.ERROR,
					"Error in getWeight for feature: '" + feature + "' (weights: " + this.weights + ")");
			throw new NullPointerException();
		}
	}

	public Application setThreshold(String feature, double threshold) {
		this.thresholds.put(feature, threshold);
		return this;
	}

	public double getThreshold(String feature) {
		try {
			return this.thresholds.get(feature);
		} catch (Exception e) {
			Logger.log(Type.ERROR,
					"Error in getThreshold for feature: '" + feature + "' (thresholds: " + this.thresholds + ")");
			throw new NullPointerException();
		}
	}

	public Application setDistanceFunction(String feature, DistanceFunction<Feature> distFunction) {
		this.distanceFunctions.put(feature, distFunction);
		return this;
	}

	public DistanceFunction<Feature> getDistanceFunction(String feature) {
		try {
			return this.distanceFunctions.get(feature);
		} catch (Exception e) {
			Logger.log(Type.ERROR,
					"Error in getDistanceFunction for feature: '" + feature + "' (distance functions: " + this.distanceFunctions + ")");
			throw new NullPointerException();
		}
	}

	public Application addFeature(String feature) {
		this.features.add(feature);
		return this;
	}

	public void normalizeWeights() {
		double total = 0;
		
		for(double weight : this.weights.values())
			total += weight;
		
		for(String feature : this.weights.keySet()) {
			this.weights.put(feature, this.weights.get(feature) / total);
		}
	}
	
}
