package br.com.lucasmaypetry.inforetrieval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.lucasmaypetry.base.Trajectory;
import br.com.lucasmaypetry.utils.StringIntPair;

public class PrecisionRecall {

	public static double[] computeFromTrajectories(List<Trajectory> trajectories, double[][] matrix) {
		// Ranking of trajectories
		List<StringIntPair> ranking = new ArrayList<>(trajectories.size());
		List<String> classes = new ArrayList<>();

		for (int i = 0; i < trajectories.size(); i++) {
			ranking.add(new StringIntPair(trajectories.get(i).getLabel(), i));
			classes.add(trajectories.get(i).getLabel());
		}

		return PrecisionRecall.compute(ranking, classes, matrix);
	}

	public static double[] computeFromClassNames(List<String> classes, double[][] matrix) {
		// Ranking of trajectories
		List<StringIntPair> ranking = new ArrayList<>(classes.size());

		for (int i = 0; i < classes.size(); i++) {
			ranking.add(new StringIntPair(classes.get(i), i));
		}

		return PrecisionRecall.compute(ranking, classes, matrix);
	}

	private static double[] compute(List<StringIntPair> ranking, List<String> classes, double[][] matrix) {
		double[][] fullMatrix = matrix;

		// Complete the upper half of the full matrix
		for (int i = 0; i < fullMatrix.length; i++)
			for (int j = i + 1; j < fullMatrix[0].length; j++)
				fullMatrix[i][j] = fullMatrix[j][i];

		double[] precisionAtRecall = { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

		int idx = 0;
		for (String cls : classes) {
			final int idxSort = idx;
			Collections.sort(ranking,
					(o1, o2) -> fullMatrix[idxSort][o1.getValue()] == fullMatrix[idxSort][o2.getValue()] ? 0
							: fullMatrix[idxSort][o1.getValue()] > fullMatrix[idxSort][o2.getValue()] ? 1 : -1);

			long classCount = 0;

			// System.out.print(cls + "\t|\t");
			//
			// for (int k = 1; k <= 5; k++)
			// System.out.print(ranking.get(k).getKey() + ", ");
			// System.out.println("");

			for (String cls2 : classes)
				if (cls.equals(cls2))
					classCount++;

			for (int recall = 1; recall <= 10; recall++) {
				long meTarget = Math.max(Math.round((classCount - 1) * recall / 10.0), 1);
				long meCount = meTarget;
				long othersCount = 0;

				for (StringIntPair t2 : ranking.subList(1, ranking.size())) {
					if (meTarget == 0)
						break;

					if (t2.getKey().equals(cls))
						meTarget--;
					else
						othersCount++;
				}

				precisionAtRecall[recall] += (double) meCount / (meCount + othersCount);
			}

			idx++;
		}

		for (int i = 1; i < precisionAtRecall.length; i++)
			precisionAtRecall[i] /= classes.size();

		return precisionAtRecall;
	}

}
