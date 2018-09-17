package br.com.lucasmaypetry.similarity;

import br.com.lucasmaypetry.base.Trajectory;

public interface SimilarityMeasure {

	public double similarityOf(Trajectory t1, Trajectory t2);

}
