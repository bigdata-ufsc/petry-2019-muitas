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
			final List<Trajectory> trajectories, int numThreads, boolean similarity) {
		final int trajSize = trajectories.size();
		
		String mxType = similarity ? "similarity" : "distance";
		Logger.log(Type.INFO, appPrefix + "Computing " + mxType + " matrix...");
		
		final int totalComp = (trajSize * trajSize) / 2 - trajSize;
		final int add = similarity ? 0 : -1;

		double[][] matrix = new double[trajSize][trajSize];
		int count = 0;

		Logger.log_dyn(Type.INFO, appPrefix + "0% complete - 0 / " + totalComp + " computations done.");
		
		for (int i = 0; i < trajSize; i++) {
			for (int j = 0; j <= i; j++) {
				matrix[i][j] = Math.abs(measure.similarityOf(trajectories.get(i), trajectories.get(j)) + add);
				count++;
			}
			int perc = (int) ((double) count / totalComp);
			Logger.log_dyn(Type.INFO, appPrefix + perc + "% complete - " + count + " / " + totalComp + " computations done.");
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
