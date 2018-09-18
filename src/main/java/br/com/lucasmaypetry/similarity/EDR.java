package br.com.lucasmaypetry.similarity;

import br.com.lucasmaypetry.base.Application;
import br.com.lucasmaypetry.base.Point;
import br.com.lucasmaypetry.base.Trajectory;

public class EDR implements SimilarityMeasure {

	private Application app;

	public EDR(Application app) {
		this.app = app;
	}

	@Override
	public double similarityOf(Trajectory t1, Trajectory t2) {
		int[][] matrix = new int[t1.length() + 1][t2.length() + 1];

		for (int k = 0; k < t1.length() + 1; k++) {
			matrix[k][0] = k;
		}

		for (int k = 0; k < t2.length() + 1; k++) {
			matrix[0][k] = k;
		}

		int i = 1;
		int j = 1;

		for (Point pointT1 : t1.getPoints()) {
			for (Point pointT2 : t2.getPoints()) {
				int cost = this.matches(pointT1, pointT2) == 0 ? 1 : 0;

				matrix[i][j] = Math.min(matrix[i - 1][j - 1] + cost,
						Math.min(matrix[i][j - 1] + 1, matrix[i - 1][j] + 1));

				j += 1;
			}

			i += 1;
			j = 1;
		}

		return 1 - matrix[t1.length()][t2.length()] / (double) Math.max(t1.length(), t2.length());
	}

	@Override
	public String toString() {
		return "EDR";
	}

	private int matches(Point p1, Point p2) {
		for (String feature : this.app.getFeatures()) {
			if (p1.getFeature(feature).matches(p2.getFeature(feature),
												  this.app.getDistanceFunction(feature),
												  this.app.getThreshold(feature)) == 0) {
				return 0;
			}
		}

		return 1;
	}

}
