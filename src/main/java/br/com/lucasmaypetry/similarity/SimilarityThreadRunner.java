package br.com.lucasmaypetry.similarity;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import br.com.lucasmaypetry.base.Trajectory;
import br.com.lucasmaypetry.utils.SyncMatrixWrapper;

public class SimilarityThreadRunner implements Runnable {

	private AtomicLong counter;
	private SyncMatrixWrapper syncMatrix;
	private List<Trajectory> trajectories;
	private SimilarityMeasure measure;
	private int begIdx;
	private int linesPerThread;
	private int add;
	
	public SimilarityThreadRunner(final AtomicLong counter, final SyncMatrixWrapper syncMatrix,
								  final List<Trajectory> trajectories, SimilarityMeasure measure,
								  int begIdx, int linesPerThread, int add) {
		this.counter = counter;
		this.syncMatrix = syncMatrix;
		this.trajectories = trajectories;
		this.measure = measure;
		this.begIdx = begIdx;
		this.linesPerThread = linesPerThread;
		this.add = add;
	}
	
	@Override
	public void run() {
		int limit = Math.min(this.begIdx + this.linesPerThread, trajectories.size());
		double[][] mx = new double[limit - this.begIdx][trajectories.size()];
		int subCount = 0;
		
		for (int i = this.begIdx; i < limit; i++) {
			for (int j = 0; j <= i; j++) {
				mx[i - this.begIdx][j] = Math.abs(measure.similarityOf(trajectories.get(i), trajectories.get(j)) + this.add);
				subCount++;
			}
			this.counter.addAndGet(subCount);
			subCount = 0;
		}
		this.syncMatrix.setLines(this.begIdx, mx);
	}

}
