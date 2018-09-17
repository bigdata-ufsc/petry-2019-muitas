package br.com.lucasmaypetry.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;
import lombok.Getter;

public class Application {

	@Getter
	private List<Integer> features;

	private Map<Integer, Double> weights;
	private Map<Integer, Double> thresholds;

	public Application() {
		this.features = new ArrayList<>();
		this.weights = new HashMap<>();
		this.thresholds = new HashMap<>();
	}

	public Application(Integer... features) {
		this.features = Arrays.asList(features);
		this.weights = new HashMap<>();
		this.thresholds = new HashMap<>();
	}

	public void clear() {
		this.features.clear();
		this.weights.clear();
		this.thresholds.clear();
	}

	public Application setWeight(int type, double weight) {
		this.weights.put(type, weight);
		return this;
	}

	public double getWeightByType(int type) {
		try {
			return this.weights.get(type);
		} catch (Exception e) {
			Logger.log(Type.ERROR,
					"Error in getWeightByType for type: '" + type + "' (weights: " + this.weights + ")");
			throw new NullPointerException();
		}
	}

	public Application setThreshold(int type, double threshold) {
		this.thresholds.put(type, threshold);
		return this;
	}

	public Application addFeature(int type) {
		this.features.add(type);
		return this;
	}

	public double getThresholdByType(int type) {
		try {
			return this.thresholds.get(type);
		} catch (Exception e) {
			Logger.log(Type.ERROR,
					"Error in getThresholdByType for type: '" + type + "' (thresholds: " + this.thresholds + ")");
			throw new NullPointerException();
		}
	}

}
