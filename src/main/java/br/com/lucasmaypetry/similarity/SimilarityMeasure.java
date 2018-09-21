package br.com.lucasmaypetry.similarity;

import br.com.lucasmaypetry.base.Trajectory;

public interface SimilarityMeasure {

	public double similarityOf(final Trajectory t1, final Trajectory t2);
	
	public SimilarityMeasure copy();
}
