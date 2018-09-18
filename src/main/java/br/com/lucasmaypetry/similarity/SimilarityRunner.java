package br.com.lucasmaypetry.similarity;

import java.util.List;

import br.com.lucasmaypetry.base.Trajectory;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;
import br.com.lucasmaypetry.similarity.SimilarityMeasure;

public class SimilarityRunner {

	public double[][] computeScores(SimilarityMeasure measure,
			final List<Trajectory> trajectories, boolean similarity) {
		String mxType = similarity ? "similarity" : "distance";
		Logger.log(Type.INFO, "Computing " + mxType + " matrix...");

		double[][] matrix = new double[trajectories.size()][trajectories.size()];
		int totalComp = (trajectories.size() * trajectories.size()) / 2 - trajectories.size();
		int step = (int) Math.ceil(totalComp / 100.0);
		int count = step;
		int perc = 0;

		Logger.log_dyn(Type.INFO, "0% of " + totalComp + " computations done.");
		
		for (int i = 0; i < trajectories.size(); i++) {
			for (int j = 0; j <= i; j++) {
				matrix[i][j] = measure.similarityOf(trajectories.get(i), trajectories.get(j));
				count--;
				
				if(count == 0) {
					count = step;
					perc++;
					Logger.log_dyn(Type.INFO, perc + "% of " + totalComp + " computations done.");
				}
			}
		}

		// Convert to distances if not similarity and complete
		// the upper half of the full matrix
		for (int i = 0; i < matrix.length; i++) {
			if(!similarity) {
				for (int j = 0; j <= i; j++)
					matrix[i][j] = 1 - matrix[j][i];
			}

			for (int j = i + 1; j < matrix[0].length; j++)
				matrix[i][j] = matrix[j][i];
		}

		Logger.log(Type.INFO, "Computing " + mxType + " matrix... DONE!");
		return matrix;
	}

}
