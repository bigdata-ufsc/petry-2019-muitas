package br.com.lucasmaypetry.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.lucasmaypetry.base.config.ComposedFeatureConfiguration;
import br.com.lucasmaypetry.base.config.DataType;
import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.base.config.FeatureConfiguration;
import br.com.lucasmaypetry.base.config.SimilarityType;
import br.com.lucasmaypetry.distance.DistanceFunction;
import br.com.lucasmaypetry.distance.ExprDistanceFunction;
import br.com.lucasmaypetry.distance.HashedExprDistanceFunction;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;
import lombok.Getter;

public class Application {

	public static String FEATURE_SEPARATOR = ",";
	
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
	
	public static Application fromExperimentConfiguration(ExperimentConfiguration config) {
		if(config.getSimilarity().equals(SimilarityType.MUITAS) && config.getFeaturesAnalysis() != null) {
			return getMUITASApplication(config);
		}

		Application app = new Application();
		Map<String, FeatureConfiguration> features = config.getFeatures();
		
		for(String feature : features.keySet()) {
			app.addFeature(feature);
			app.setWeight(feature, features.get(feature).getWeight());
			app.setThreshold(feature, features.get(feature).getThresholds().get(0));
			
			if(features.get(feature).getType() == DataType.STRING ||
					features.get(feature).getType() == DataType.DATE) {
				app.setDistanceFunction(feature, new HashedExprDistanceFunction(features.get(feature).getDistanceFunction()));
			} else {
				app.setDistanceFunction(feature, new ExprDistanceFunction(features.get(feature).getDistanceFunction()));
			}
		}
		
		app.normalizeWeights();
		return app;
	}

	private static Application getMUITASApplication(ExperimentConfiguration config) {
		Set<String> features = new HashSet<>();
		Application app = new Application();
		
		for(ComposedFeatureConfiguration cfc : config.getFeaturesAnalysis()) {
			String compFeature = "";
			
			for(String f : cfc.getFeatures()) {
				compFeature += FEATURE_SEPARATOR + f;
				features.add(f);
			}

			compFeature = compFeature.substring(1);
			app.addFeature(compFeature);
			app.setWeight(compFeature, cfc.getWeight());
		}
		
		Map<String, FeatureConfiguration> featuresCfg = config.getFeatures();
		
		for(String feature : features) {
			app.setThreshold(feature, featuresCfg.get(feature).getThresholds().get(0));
			
			if(featuresCfg.get(feature).getType() == DataType.STRING ||
					featuresCfg.get(feature).getType() == DataType.DATE) {
				app.setDistanceFunction(feature, new HashedExprDistanceFunction(featuresCfg.get(feature).getDistanceFunction()));
			} else {
				app.setDistanceFunction(feature, new ExprDistanceFunction(featuresCfg.get(feature).getDistanceFunction()));
			}
		}
		
		app.normalizeWeights();
		return app;
	}
}
