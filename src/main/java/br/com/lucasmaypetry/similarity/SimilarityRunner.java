package br.com.lucasmaypetry.similarity;

import java.util.List;

import br.com.lucasmaypetry.base.Application;
import br.com.lucasmaypetry.base.Trajectory;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;
import br.com.lucasmaypetry.similarity.SimilarityMeasure;

public class SimilarityRunner {
	
	private String appPrefix;
	
	public SimilarityRunner(Application application) {
		this.appPrefix = application.getPrefix() + " - ";
	}

	public double[][] computeScores(SimilarityMeasure measure,
			final List<Trajectory> trajectories, boolean similarity) {
		String mxType = similarity ? "similarity" : "distance";
		Logger.log(Type.INFO, appPrefix + "Computing " + mxType + " matrix...");

		double[][] matrix = new double[trajectories.size()][trajectories.size()];
		int totalComp = (trajectories.size() * trajectories.size()) / 2 - trajectories.size();
		int step = (int) Math.ceil(totalComp / 100.0);
		int count = step;
		int perc = 0;
		int add = similarity ? 0 : -1;

		Logger.log_dyn(Type.INFO, appPrefix + "0% of " + totalComp + " computations done.");
		
		for (int i = 0; i < trajectories.size(); i++) {
			for (int j = 0; j <= i; j++) {
				matrix[i][j] = Math.abs(measure.similarityOf(trajectories.get(i), trajectories.get(j)) + add);
				count--;
				
				if(count == 0) {
					count = step;
					perc++;
					Logger.log_dyn(Type.INFO, appPrefix + perc + "% of " + totalComp + " computations done.");
				}
			}
		}

		// Complete the upper half of the full matrix
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix[0].length; j++)
				matrix[i][j] = matrix[j][i];
		}

		Logger.log(Type.INFO, appPrefix + "Computing " + mxType + " matrix... DONE!");
		return matrix;
	}

}
