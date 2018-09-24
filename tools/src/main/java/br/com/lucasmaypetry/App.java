package br.com.lucasmaypetry;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.lucasmaypetry.base.config.AppArgs;
import br.com.lucasmaypetry.base.config.EvaluationMetric;
import br.com.lucasmaypetry.inforetrieval.MeanReciprocalRank;
import br.com.lucasmaypetry.inforetrieval.PrecisionRecall;
import br.com.lucasmaypetry.utils.ArgParser;
import br.com.lucasmaypetry.utils.CSVReader;
import br.com.lucasmaypetry.utils.CSVWriter;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;

public class App {
	
    public static void main(String[] args) {
    	AppArgs procArgs = new ArgParser().parse(args);
    	Map<String, String> tidToClass = new HashMap<>();

		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.setMinimumFractionDigits(4);
		df.setMaximumFractionDigits(4);
		df.setMinimumIntegerDigits(1);
		df.setMaximumIntegerDigits(3);
    	
    	// Create class dictionary
    	if(procArgs.getClassFile() != null) {
    		Logger.log(Type.INFO, "Building class dictionary... ");
    		
			try {
				CSVReader reader = new CSVReader(procArgs.getClassFile());
    			String[] header = reader.readLineAsArray();
    			int tidIdx = 0;
    			int classIdx = 1;
    			
    			if(header[1].equals("tid")) {
    				tidIdx = 1;
    				classIdx = 0;
    			}
    			
    			int lines = reader.lineCount();
    			
	    		for(int i = 1; i < lines; i++) {
	    			String[] line = reader.readLineAsArray();
	    			tidToClass.put(line[tidIdx], line[classIdx]);
	    		}
	    		reader.close();
			} catch (IOException e) {
				Logger.log(Type.ERROR, e.getMessage());
				e.printStackTrace();
			}
    		Logger.log(Type.INFO, "Building class dictionary... DONE!");
    	}
    	
    	CSVWriter writer = null;
    	List<EvaluationMetric> metrics = procArgs.getMetrics();
    	
    	try {
			writer = new CSVWriter(procArgs.getOutputFile());
			String[] cols = new String[2 + metrics.size()];
			cols[0] = "#";
			cols[1] = "file";
			
			for(int i = 2; i < metrics.size() + 2; i++)
				cols[i] = metrics.get(i - 2).toString();
			
	    	writer.writeLine(cols);
		} catch (IOException e) {
			Logger.log(Type.ERROR, e.getMessage());
			e.printStackTrace();
		}
    	
    	int counter = 0;
    	
    	for(String file : procArgs.getInputFiles()) {
    		Logger.log_dyn(Type.INFO, "Computing metrics for file '" + file + "'... ");
    		try {
				CSVReader reader = new CSVReader(file);
				String[] classList = reader.readLineAsArray();
				double[][] matrix = reader.readAsDoubleMatrix();
				reader.close();
				
				if(!procArgs.getDistances()) {
					for(int i = 0; i < matrix.length; i++)
						for(int j = 0; j < matrix[i].length; j++)
							matrix[i][j] = 1 - matrix[i][j];
				}
				
				if(tidToClass.size() > 0) {
					for(int i = 0; i < classList.length; i++)
						classList[i] = tidToClass.get(classList[i]);
				}
				
				String[] cols = new String[2 + metrics.size()];
				cols[0] = ++counter + "";
				cols[1] = file;
				
				for(int i = 0; i < metrics.size(); i++) {
					switch(metrics.get(i)) {
					case MAP:
						double[] pr = PrecisionRecall.computeFromClassNames(Arrays.asList(classList), matrix);
						double map = 0;
						
						for(double score : pr)
							map += score;
						
						cols[i + 2] = df.format(map / pr.length);
						break;
					case MRR:
						double mrr = MeanReciprocalRank.computeFromClassNames(Arrays.asList(classList), matrix);
						cols[i + 2] = df.format(mrr);
						break;
					case HCA:
						cols[i + 2] = "TO-DO";
						break;
					}
				}
				
				writer.writeLine(cols);
				writer.flush();
			} catch (IOException e) {
				Logger.log(Type.ERROR, e.getMessage());
				e.printStackTrace();
			}
    		
    		Logger.log_dyn(Type.INFO, "Computing metrics for file '" + file + "'... DONE!");
    	}
    	
		try {
			writer.close();
		} catch (IOException e) {
			Logger.log(Type.ERROR, e.getMessage());
			e.printStackTrace();
		}

		Logger.log_dyn(Type.INFO, "Evaluation finished!");
    }
    
}
