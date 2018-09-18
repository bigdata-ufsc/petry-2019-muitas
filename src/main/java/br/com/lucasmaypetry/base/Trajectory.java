package br.com.lucasmaypetry.base;

import java.util.List;

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

}
