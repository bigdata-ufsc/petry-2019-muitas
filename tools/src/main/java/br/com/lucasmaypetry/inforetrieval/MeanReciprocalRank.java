package br.com.lucasmaypetry.inforetrieval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.lucasmaypetry.utils.StringIntPair;

public class MeanReciprocalRank {

	public static double computeFromClassNames(List<String> classes, double[][] matrix) {
		// Ranking of trajectories
		List<StringIntPair> ranking = new ArrayList<>(classes.size());

		for (int i = 0; i < classes.size(); i++) {
			ranking.add(new StringIntPair(classes.get(i), i));
		}

		return MeanReciprocalRank.compute(ranking, classes, matrix);
	}

	private static double compute(List<StringIntPair> ranking, List<String> classes, double[][] matrix) {
		double[][] fullMatrix = matrix;

		// Complete the upper half of the full matrix
		for (int i = 0; i < fullMatrix.length; i++)
			for (int j = i + 1; j < fullMatrix[0].length; j++)
				fullMatrix[i][j] = fullMatrix[j][i];

		double mrr = 0;

		int idx = 0;
		for (String cls : classes) {
			final int idxSort = idx;
			Collections.sort(ranking,
					(o1, o2) -> fullMatrix[idxSort][o1.getValue()] == fullMatrix[idxSort][o2.getValue()] ? 0
							: fullMatrix[idxSort][o1.getValue()] > fullMatrix[idxSort][o2.getValue()] ? 1 : -1);

			int rank = 1;

			for (StringIntPair t2 : ranking.subList(1, ranking.size())) {
				if (!t2.getKey().equals(cls))
					rank++;
				else
					break;
			}

			mrr += 1.0 / rank;
			idx++;
		}

		return mrr / classes.size();
	}
	
}
