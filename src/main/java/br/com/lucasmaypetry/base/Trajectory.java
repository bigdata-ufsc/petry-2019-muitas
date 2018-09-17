package br.com.lucasmaypetry.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;
import lombok.Getter;

@Getter
public class Trajectory {

	private int tid;
	private List<Point> points;
	private String label;

	public Trajectory(int tid, List<Point> points, String label) {
		this.tid = tid;
		this.points = points;
		this.label = label;
	}

	public int length() {
		return this.points.size();
	}

	public Pair<Trajectory, Trajectory> randomlySplitInHalf(long seed) {
		List<Point> allPoints = new ArrayList<>(this.points);
		Random rand = new Random(seed);

		List<Point> points1 = new ArrayList<>();

		for (int i = 0; i < this.points.size() / 2; i++) {
			points1.add(allPoints.remove(rand.nextInt(allPoints.size())));
		}

		List<Point> points2 = allPoints;

		return new Pair<>(new Trajectory(-1, points1, this.label),
				new Trajectory(-1, points2, this.label));
	}
}
