package br.com.lucasmaypetry.utils;

import java.util.Arrays;

import lombok.Getter;

@Getter
public class SyncMatrixWrapper {

	private double[][] matrix;
	
	public SyncMatrixWrapper(int height, int width) {
		this.matrix = new double[height][width];
	}
	
	public synchronized void setLines(int firstRow, double[][] lines) {
		for(int i = 0; i < lines.length; i++)
			this.matrix[firstRow + i] = Arrays.copyOf(lines[i], lines[i].length);
	}
	
}
