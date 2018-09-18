package br.com.lucasmaypetry.experiment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.lucasmaypetry.base.Application;
import br.com.lucasmaypetry.base.config.ComposedFeatureConfiguration;
import br.com.lucasmaypetry.base.config.DataType;
import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.base.config.FeatureConfiguration;
import br.com.lucasmaypetry.base.config.SimilarityType;
import br.com.lucasmaypetry.distance.ExprDistanceFunction;
import br.com.lucasmaypetry.distance.HashedExprDistanceFunction;

public class ApplicationFactory {

	public static List<Application> buildApplicationsFromConfig(ExperimentConfiguration config) {
		List<Application> apps = new ArrayList<>();
		List<List<Double>> thresholds = new ArrayList<>();
		List<List<Double>> combinations = new ArrayList<>();
		List<String> features = new ArrayList<>(config.getFeatures().keySet());
		
		for(FeatureConfiguration fc : config.getFeatures().values()) {
			thresholds.add(fc.getThresholds());
		}

		generateThresholdCombinations(thresholds, combinations, 0, new ArrayList<>());

		for(List<Double> combination : combinations) {
			Application app = getDefaultApp(config);
			for(int i = 0; i < features.size(); i++)
				app.setThreshold(features.get(i), combination.get(i));
			
			apps.add(app);
		}

		return apps;
	}
	
	private static void generateThresholdCombinations(List<List<Double>> lists, List<List<Double>> result, int depth, List<Double> current) {
	    if(depth == lists.size()) {
	       result.add(current);
	       return;
	     }

	    for(int i = 0; i < lists.get(depth).size(); ++i) {
	    	List<Double> newCurrent = new ArrayList<>(current);
	    	newCurrent.add(lists.get(depth).get(i));
	        generateThresholdCombinations(lists, result, depth + 1, newCurrent);
	    }
	}

	private static Application getDefaultApp(ExperimentConfiguration config) {
		if(config.getSimilarity().equals(SimilarityType.MUITAS) && config.getFeaturesAnalysis() != null) {
			return getMUITASApplication(config);
		}

		Application app = new Application();
		Map<String, FeatureConfiguration> features = config.getFeatures();
		
		for(String feature : features.keySet()) {
			app.addFeature(feature);
			app.setWeight(feature, features.get(feature).getWeight());
			
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
				compFeature += Application.FEATURE_SEPARATOR + f;
				features.add(f);
			}

			compFeature = compFeature.substring(1);
			app.addFeature(compFeature);
			app.setWeight(compFeature, cfc.getWeight());
		}
		
		Map<String, FeatureConfiguration> featuresCfg = config.getFeatures();
		
		for(String feature : features) {			
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
