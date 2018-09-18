package br.com.lucasmaypetry.base;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.lucasmaypetry.base.config.FeatureConfiguration;
import br.com.lucasmaypetry.base.config.DataType;
import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.distance.ExprDistanceFunction;
import br.com.lucasmaypetry.distance.HashedExprDistanceFunction;
import br.com.lucasmaypetry.similarity.SimilarityMeasure;
import br.com.lucasmaypetry.similarity.SimilarityRunner;
import br.com.lucasmaypetry.utils.CSVWriter;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;

public class ExperimentRunner {

	private ExperimentConfiguration config;
	
	public ExperimentRunner(ExperimentConfiguration config) {
		this.config = config;
	}
	
	public void run(List<Trajectory> trajectories, String outputFile) {
		Logger.log(Type.INFO, "Running experiment '" + this.config.getExperimentName() + "'... ");
//		ExecutorService executor = Executors.newFixedThreadPool(config.getThreads());
		Application app = this.getApplication();
		SimilarityMeasure measure = null;
		
		try {
			measure = this.config.getSimilarity().getMeasure(app);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			Logger.log(Type.ERROR, e.getMessage());
			e.printStackTrace();
			return;
		}
		
		double[][] sim = new SimilarityRunner().computeScores(measure, trajectories, !config.getComputeDistances());
		
		Logger.log(Type.INFO, "Writing similarity/distance matrix to file '" + outputFile + "'... ");
		try {
			CSVWriter mxWriter = new CSVWriter(outputFile);
			List<String> tids = new ArrayList<>();
			trajectories.stream().sequential().forEach((t) -> tids.add(t.getTid() + ""));
			
			mxWriter.writeLine(tids.toArray(new String[tids.size()]));
			
			for(int i = 0; i < sim.length; i++) {
				mxWriter.writeLine(sim[i]);
				mxWriter.flush();
			}
			
			mxWriter.close();
			Logger.log(Type.INFO, "Writing similarity/distance matrix to file '" + outputFile + "'... DONE!");
		} catch (IOException e) {
			Logger.log(Type.ERROR, e.getMessage());
			e.printStackTrace();
		}
		
//		executor.shutdown();
//
//		try {
//			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		if (!executor.isTerminated()) {
//			Logger.log(Type.WARNING, "Not all tasks have finished!");
//		} else {		
//			Logger.log(Type.INFO, "All tasks have finished!");
//		}
		Logger.log(Type.INFO, "Running experiment '" + this.config.getExperimentName() + "'... DONE!");
	}
	
	private Application getApplication() {
		Application app = new Application();
		Map<String, FeatureConfiguration> features = this.config.getFeatures();
		
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
	
}
