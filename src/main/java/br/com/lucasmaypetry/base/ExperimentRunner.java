package br.com.lucasmaypetry.base;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
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
		Application app = Application.fromExperimentConfiguration(this.config);
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
	
}
