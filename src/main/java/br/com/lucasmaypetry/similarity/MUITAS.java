package br.com.lucasmaypetry.similarity;

import br.com.lucasmaypetry.base.Application;
import br.com.lucasmaypetry.base.Point;
import br.com.lucasmaypetry.base.Trajectory;

public class MUITAS implements SimilarityMeasure {

	private Application app;

	public MUITAS(Application app) {
		this.app = app;
	}

	@Override
	public double similarityOf(Trajectory t1, Trajectory t2) {
		double[][] scores = new double[t1.length()][t2.length()];
		double parityT1T2 = 0;
		double parityT2T1 = 0;
		
		for(int i = 0; i < t1.length(); i++) {
			double maxCol = 0;
			
			for(int j = 0; j < t2.length(); j++) {
				scores[i][j] = this.score(t1.getPoints().get(i), t2.getPoints().get(j));
				maxCol = scores[i][j] > maxCol ? scores[i][j] : maxCol;
			}
			
			parityT1T2 += maxCol;
		}
		
		for(int j = 0; j < t2.length(); j++) {
			double maxRow = 0;
			
			for(int i = 0; i < t1.length(); i++) {
				scores[i][j] = this.score(t1.getPoints().get(i), t2.getPoints().get(j));
				maxRow = scores[i][j] > maxRow ? scores[i][j] : maxRow;
			}
			
			parityT2T1 += maxRow;
		}
		
		return (parityT1T2 + parityT2T1) / (t1.length() + t2.length());
	}

	@Override
	public String toString() {
		return "MUITAS";
	}

	private double score(Point p1, Point p2) {
		double total = 0;

		for (String compFeature : this.app.getFeatures()) {
			String[] features = compFeature.split("\\" + Application.FEATURE_SEPARATOR);
			int match = 1;

			for (String f: features) {
				if (p1.getFeature(f).matches(p2.getFeature(f),
						   					 this.app.getDistanceFunction(f),
						   					 this.app.getThreshold(f)) == 0) {
					match = 0;
					break;
				}
			}

			total += match * this.app.getWeight(compFeature);
		}

		return total;
	}

}
