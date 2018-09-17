package br.com.lucasmaypetry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lucasmaypetry.base.Attribute;
import br.com.lucasmaypetry.base.ExperimentRunner;
import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.base.config.SimilarityType;
import br.com.lucasmaypetry.distance.ExpressionDistanceFunction;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class App {
	
    public static void main( String[] args ) {
    	ArgumentParser parser = ArgumentParsers.newFor("Trajectory Similarity").build()
                .defaultHelp(true)
                .description("Compute the similarity of trajectories.");
        parser.addArgument("-s", "--similarity")
                .choices("LCSS", "EDR", "MSM")
                .help("specify the similarity measure to use");
        parser.addArgument("input").nargs("*")
                .help("trajectory file(s) to compute the similarity");
        parser.addArgument("output").nargs(1)
        		.help("output file with the similarity scores");
        parser.addArgument("config").nargs(1)
				.help("configuration file");
        Namespace ns = null;
        
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        List<String> inputFile = formatFileNames(ns.getList("input"));
        String outputFile = formatFileName(ns.getString("output")
				.replaceAll("\\[", "").replaceAll("\\]", ""));
        String configFile = formatFileName(ns.getString("config")
        		.replaceAll("\\[", "").replaceAll("\\]", ""));
        String similarityMeasure = ns.getString("similarity");

        Logger.log(Type.INFO, "Input file(s): " + inputFile);
        Logger.log(Type.INFO, "Output file:   " + outputFile);
        Logger.log(Type.INFO, "Config file:   " + configFile);
        
		ObjectMapper mapper = new ObjectMapper();
		ExperimentConfiguration c = null;
		
		try {
			c = mapper.readValue(new File(configFile), ExperimentConfiguration.class);
		} catch (IOException e) {
			Logger.log(Type.ERROR, e.getMessage());
			e.printStackTrace();
		}
		
		if(similarityMeasure != null) {
			c.setSimilarity(SimilarityType.valueOf(similarityMeasure));
		}

        Logger.log(Type.INFO, "Similarity:    " + c.getSimilarity());
		ExperimentRunner experiment = new ExperimentRunner(c);
		experiment.run();
//        System.out.println(new ExpressionDistanceFunction("abs(x + y)").distance(new Attribute(1, -20.3), new Attribute(1, 1.5)));
    }
    
    private static String formatFileName(String file) {
		String curDir = System.getProperty("user.dir");
		boolean backslash = curDir.contains("/") ? false : true;
		
		if(!file.contains("\\") && !file.contains("/")) {
			return backslash ? curDir + "\\" + file : curDir + "/" + file;
		}
		
		return file;
    }

    private static List<String> formatFileNames(List<String> files) {
    	List<String> newFiles = new ArrayList<>(files.size());
    	
    	for(String file : files) {
    		newFiles.add(formatFileName(file));
    	}
    	
    	return newFiles;
    }
    
}
