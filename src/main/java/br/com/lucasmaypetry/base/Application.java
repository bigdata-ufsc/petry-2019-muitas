package br.com.lucasmaypetry.base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.lucasmaypetry.distance.DistanceFunction;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;
import lombok.Getter;

public class Application {

	public static String FEATURE_SEPARATOR = ".";
	private static AtomicInteger APP_NUMBER = new AtomicInteger(0);
	
	@Getter
	private List<String> features;

	@Getter
	private int number;
	
	private Map<String, Double> weights;
	private Map<String, Double> thresholds;
	private Map<String, DistanceFunction<Feature>> distanceFunctions;

	public Application() {
		this.number = APP_NUMBER.incrementAndGet();
		this.features = new ArrayList<>();
		this.weights = new HashMap<>();
		this.thresholds = new HashMap<>();
		this.distanceFunctions = new HashMap<>();
	}

	public Application(String... features) {
		this.number = APP_NUMBER.incrementAndGet();
		this.features = Arrays.asList(features);
		this.weights = new HashMap<>();
		this.thresholds = new HashMap<>();
	}
	
	@Override
	public synchronized Application clone() {
		APP_NUMBER.decrementAndGet();
		Application app = new Application();
		app.features = new ArrayList<>(this.features);
		app.number = this.number;
		app.weights = new HashMap<>(this.weights);
		app.thresholds = new HashMap<>(this.thresholds);
		app.distanceFunctions = new HashMap<>(this.distanceFunctions);
		
		return app;
	}
	
	public String getPrefix() {
		return "App " + this.number;
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
	
	public void toFile(String file) throws IOException {
		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.setMinimumFractionDigits(4);
		df.setMaximumFractionDigits(4);
		df.setMinimumIntegerDigits(1);
		df.setMaximumIntegerDigits(3);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("==================================================================\n");
		writer.write(" Application " + this.number + "\n");
		writer.write("==================================================================\n\n");
		writer.write(" Features\n");
		writer.write("------------------------------------------------------------------\n");
		
		if(this.features.size() == this.thresholds.keySet().size()) {
			for(String feature : this.thresholds.keySet()) {
				writer.write(feature + ", weight = " + df.format(this.weights.get(feature)) +
									   ", threshold = " + df.format(this.thresholds.get(feature)) +
									   ", distance = " + this.distanceFunctions.get(feature) + "\n");
			}		
		} else {
			for(String feature : this.thresholds.keySet()) {
				writer.write(feature + ", threshold = " + df.format(this.thresholds.get(feature)) +
									   ", distance = " + this.distanceFunctions.get(feature) + "\n");
			}

			writer.write("\nComposed Features:  " + this.weights.keySet() + "\n");
			writer.write("Weights:            " + this.weights.values() + "\n\n");
		}
		writer.flush();
		writer.close();
	}
}
