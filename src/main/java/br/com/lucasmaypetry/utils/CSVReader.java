package br.com.lucasmaypetry.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CSVReader {

	private BufferedReader reader;
	private String file;
	private DecimalFormat df;
	private String separator;

	public CSVReader(String file) throws IOException {
		this.reader = new BufferedReader(new FileReader(file));
		this.file = file;
		this.separator = ",";
		this.df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		this.df.setMinimumFractionDigits(4);
		this.df.setMaximumFractionDigits(4);
		this.df.setMinimumIntegerDigits(1);
		this.df.setMaximumIntegerDigits(3);
	}

	public CSVReader(String file, String separator) throws IOException {
		this.reader = new BufferedReader(new FileReader(file));
		this.file = file;
		this.separator = separator;
		this.df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		this.df.setMinimumFractionDigits(4);
		this.df.setMaximumFractionDigits(4);
		this.df.setMinimumIntegerDigits(1);
		this.df.setMaximumIntegerDigits(3);
	}

	public synchronized String readLine() throws IOException {
		return this.reader.readLine();
	}

	public synchronized List<List<String>> readAsColumnStringList() throws IOException {
		int count = this.lineCount();
		Iterator<String> it = this.reader.lines().iterator();

		List<List<String>> list = new ArrayList<>();

		int idx = 0;
		while (it.hasNext()) {
			String[] columns = it.next().split(this.separator);

			if (idx == 0) {
				for (int i = 0; i < columns.length; i++)
					list.add(new ArrayList<>(count));
			}

			for (int i = 0; i < columns.length; i++) {
				list.get(i).add(columns[i]);
			}

			idx++;
		}

		return list;
	}

	public synchronized double[][] readAsDoubleMatrix() throws IOException {
		int count = this.lineCount();
		Iterator<String> it = this.reader.lines().iterator();

		double[][] matrix = new double[count][count];

		int idx = 0;
		while (it.hasNext()) {
			String[] columns = it.next().split(this.separator);
			matrix[idx] = this.toDouble(columns);
			idx++;
		}

		return matrix;
	}

	public int lineCount() throws IOException {
		BufferedReader readerCount = new BufferedReader(new FileReader(this.file));
		try {
			return (int) readerCount.lines().count();
		} finally {
			readerCount.close();
		}
	}

	public void close() throws IOException {
		this.reader.close();
	}

	private double[] toDouble(String[] strings) {
		double[] v = new double[strings.length];

		for (int i = 0; i < strings.length; i++)
			v[i] = Double.parseDouble(strings[i]);

		return v;
	}

}
