package br.com.lucasmaypetry.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.lucasmaypetry.base.config.AppArgs;
import br.com.lucasmaypetry.base.config.EvaluationMetric;
import br.com.lucasmaypetry.utils.Logger.Type;
import lombok.NoArgsConstructor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

@NoArgsConstructor
public class ArgParser {
	
	public AppArgs parse(String[] args) {
    	ArgumentParser parser = ArgumentParsers.newFor("Evaluation Tools").build()
                .defaultHelp(true)
                .description("Evaluate results of similarity measures.");
        parser.addArgument("-m", "--metric").nargs("*")
                .choices("MAP", "MRR", "HCA")
                .help("specify the metric(s) of evaluation to compute");
        parser.addArgument("-d", "--distances")
		        .choices("true", "false")
		        .setDefault("true")
		        .help("specify whether the matrix contains distances (true) or similarities (false)");
        parser.addArgument("-e", "--ext")
        		.setDefault("csv")
        		.help("specify the input file(s) extension");
        parser.addArgument("-c", "--class-file")
				.help("specify the file containing the class dictionary according to the matrix header");
        parser.addArgument("input").nargs(1)
                .help("folder or file to evaluate");
        parser.addArgument("output").nargs(1)
        		.help("output file with the results");
        Namespace ns = null;
        
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        AppArgs ret = new AppArgs();
        List<EvaluationMetric> metrics = new ArrayList<>();
        for(Object m : ns.getList("metric")) {
        	metrics.add(EvaluationMetric.valueOf((String) m));
        }
                
        ret.setOutputFile(this.formatString(ns.getString("output")
									.replaceAll("\\[", "").replaceAll("\\]", "")));
        ret.setClassFile(this.formatString(ns.getString("class_file")
									.replaceAll("\\[", "").replaceAll("\\]", "")));
        ret.setDistances(Boolean.parseBoolean(ns.getString("distances")));
        ret.setInputExtension(ns.getString("ext")
									.replaceAll("\\[", "").replaceAll("\\]", ""));
        ret.setMetrics(metrics);

        String inputStr = this.formatString(ns.getString("input")
				.replaceAll("\\[", "").replaceAll("\\]", ""));
        File input = new File(inputStr);
        
        if(input.isDirectory()) {
        	List<String> files = new ArrayList<>();
        	
        	for(String s : input.list()) {
        		String[] pieces = s.split("\\.");
        		
        		if(pieces[pieces.length - 1].equals(ret.getInputExtension())) {
        			files.add(inputStr + "/" + s);
        		}
        	}
        	
        	Collections.sort(files);
        	ret.setInputFiles(files);
        } else {
        	ret.setInputFiles(Arrays.asList(inputStr));
        }

        Logger.log(Type.INFO, "Input folder/file:  " + inputStr);
        Logger.log(Type.INFO, "Input extension:    " + ret.getInputExtension());
        Logger.log(Type.INFO, "Output file:        " + ret.getOutputFile());
        Logger.log(Type.INFO, "Class dict. file:   " + ret.getClassFile());
        Logger.log(Type.INFO, "Distance matrix:    " + ret.getDistances());
        Logger.log(Type.INFO, "Evaluation metrics: " + ret.getMetrics());
        
        return ret;
	}

    private String formatString(String file) {
		String curDir = System.getProperty("user.dir");
		boolean backslash = curDir.contains("/") ? false : true;
		
		if(!file.contains("\\") && !file.contains("/")) {
			return backslash ? curDir + "\\" + file : curDir + "/" + file;
		}
		
		return file;
    }

}
