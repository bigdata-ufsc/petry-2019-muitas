package br.com.lucasmaypetry.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CSVWriter {

	private BufferedWriter writer;
	private DecimalFormat df;

	public CSVWriter(String file) throws IOException {
		this.writer = new BufferedWriter(new FileWriter(file));
		this.df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		this.df.setMinimumFractionDigits(4);
		this.df.setMaximumFractionDigits(4);
		this.df.setMinimumIntegerDigits(1);
		this.df.setMaximumIntegerDigits(3);
	}

	public CSVWriter(String file, boolean append) throws IOException {
		this.writer = new BufferedWriter(new FileWriter(file, append));
		this.df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		this.df.setMinimumFractionDigits(4);
		this.df.setMaximumFractionDigits(4);
		this.df.setMinimumIntegerDigits(1);
		this.df.setMaximumIntegerDigits(3);
	}

	public synchronized void writeLine(String... strings) throws IOException {
		String line = "";

		for (String s : strings)
			line += s + ",";

		this.writer.write(line.substring(0, line.length() - 1) + "\n");
	}

	public synchronized void writeLine(double... numbers) throws IOException {
		String line = "";

		for (double n : numbers)
			line += this.df.format(n) + ",";

		this.writer.write(line.substring(0, line.length() - 1) + "\n");
	}

	public synchronized void flush() throws IOException {
		this.writer.flush();
	}

	public void close() throws IOException {
		this.writer.close();
	}

}
