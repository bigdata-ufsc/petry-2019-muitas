package br.com.lucasmaypetry.experiment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.com.lucasmaypetry.base.Application;
import br.com.lucasmaypetry.base.Trajectory;
import br.com.lucasmaypetry.base.config.AppArgs;
import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.data.TrajectoryLoader;
import br.com.lucasmaypetry.similarity.SimilarityMeasure;
import br.com.lucasmaypetry.similarity.SimilarityRunner;
import br.com.lucasmaypetry.utils.CSVWriter;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;

public class ExperimentRunner {

	private ExperimentConfiguration config;
	private AppArgs args;
	
	public ExperimentRunner(ExperimentConfiguration config, AppArgs args) {
		this.config = config;
		this.args = args;
	}
	
	public void run() {
        TrajectoryLoader loader = new TrajectoryLoader(this.args.getInputFile());
        List<Trajectory> trajectories = loader.load(config);
        
		Logger.log(Type.INFO, "Running experiment '" + this.config.getExperimentName() + "'... ");
		ExecutorService executor = Executors.newFixedThreadPool(this.args.getThreads());
		List<Application> apps = ApplicationFactory.buildApplicationsFromConfig(this.config, this.args.getSimilarity());
		Logger.log(Type.INFO, apps.size() + " application(s) built!");
		
		int idxExt = this.args.getOutputFile().lastIndexOf(".");
		String outPrefix = this.args.getOutputFile().substring(0, idxExt != -1 ? idxExt : this.args.getOutputFile().length());
		String ext = idxExt != -1 ? this.args.getOutputFile().substring(idxExt) : ".csv";
				
		for(Application app : apps) {
			executor.submit(() -> {
				SimilarityMeasure measure = null;
				String pfx = app.getPrefix() + " - ";
				Logger.log(Type.INFO, "Running application " + app.getNumber() + "... ");
				String outFilePrefix = outPrefix + "_" + app.getNumber();

				if(apps.size() == 1) {
					outFilePrefix = outPrefix;
				}
				
				try {
					measure = this.args.getSimilarity().getMeasure(app);
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					Logger.log(Type.ERROR, pfx + e.getMessage());
					e.printStackTrace();
					return;
				}
				
				double[][] sim = new SimilarityRunner(app).computeScores(measure, trajectories, this.args.getThreads(), !this.args.getComputeDistances());
				
				Logger.log(Type.INFO, pfx + "Writing similarity/distance matrix to file '" + this.args.getOutputFile() + "'... ");
				try {
					CSVWriter mxWriter = new CSVWriter(outFilePrefix + ext);
					List<String> tids = new ArrayList<>();
					trajectories.stream().sequential().forEach((t) -> tids.add(t.getTid() + ""));
					
					mxWriter.writeLine(tids.toArray(new String[tids.size()]));
					
					for(int i = 0; i < sim.length; i++) {
						mxWriter.writeLine(sim[i]);
						mxWriter.flush();
					}
					
					mxWriter.close();
					Logger.log(Type.INFO, pfx + "Writing similarity/distance matrix to file '" + this.args.getOutputFile() + "'... DONE!");
					app.toFile(outFilePrefix + ".txt");
					Logger.log(Type.INFO, "Running application " + app.getNumber() + "... DONE!");
				} catch (IOException e) {
					Logger.log(Type.ERROR, pfx + e.getMessage());
					e.printStackTrace();
				}
			});
		}
		
		executor.shutdown();

		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!executor.isTerminated()) {
			Logger.log(Type.WARNING, "Not all tasks have finished!");
		} else {		
			Logger.log(Type.INFO, "All tasks have finished!");
		}
		Logger.log(Type.INFO, "Running experiment '" + this.config.getExperimentName() + "'... DONE!");
	}
	
}
