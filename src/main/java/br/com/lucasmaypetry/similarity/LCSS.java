package br.com.lucasmaypetry.similarity;

import br.com.lucasmaypetry.base.Application;
import br.com.lucasmaypetry.base.Point;
import br.com.lucasmaypetry.base.Trajectory;

public class LCSS implements SimilarityMeasure {

	private Application app;

	public LCSS(Application app) {
		this.app = app;
	}

	@Override
	public final double similarityOf(Trajectory t1, Trajectory t2) {
		int[][] matrix = new int[t1.length() + 1][t2.length() + 1];
		int i = 1;
		int j = 1;

		for (Point pointT1 : t1.getPoints()) {
			for (Point pointT2 : t2.getPoints()) {
				if (this.matches(pointT1, pointT2) == 1) {
					matrix[i][j] = matrix[i - 1][j - 1] + 1;
				} else {
					matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
				}

				j += 1;
			}

			i += 1;
			j = 1;
		}

		return matrix[t1.length()][t2.length()] / (double) Math.min(t1.length(), t2.length());
	}

	@Override
	public String toString() {
		return "LCSS";
	}

	private final int matches(Point p1, Point p2) {
		for (String feature : this.app.getFeatures()) {
			if (p1.getFeature(feature).matches(p2.getFeature(feature),
												  this.app.getDistanceFunction(feature),
												  this.app.getThreshold(feature)) == 0) {
				return 0;
			}
		}

		return 1;
	}

	@Override
	public SimilarityMeasure copy() {
		return new LCSS(this.app.clone());
	}

}
