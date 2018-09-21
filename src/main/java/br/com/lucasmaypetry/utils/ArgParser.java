package br.com.lucasmaypetry.utils;

import br.com.lucasmaypetry.base.config.AppArgs;
import br.com.lucasmaypetry.base.config.SimilarityType;
import br.com.lucasmaypetry.utils.Logger.Type;
import lombok.NoArgsConstructor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

@NoArgsConstructor
public class ArgParser {
	
	public AppArgs parse(String[] args) {
    	ArgumentParser parser = ArgumentParsers.newFor("Trajectory Similarity").build()
                .defaultHelp(true)
                .description("Compute distances/similarities of trajectories.");
        parser.addArgument("-s", "--similarity")
                .choices("LCSS", "EDR", "MSM", "MUITAS")
                .help("specify the similarity measure to use");
        parser.addArgument("-d", "--compute-distances")
		        .choices("true", "false")
		        .setDefault("false")
		        .help("specify whether to compute distances (true) or similarities (false)");
        parser.addArgument("-t", "--threads")
		        .setDefault("1")
		        .help("the number of threads to be created for running the experiment");
        parser.addArgument("input").nargs(1)
                .help("trajectory file to compute distances/similarities");
        parser.addArgument("output").nargs(1)
        		.help("output file with the distance/similarity scores");
        parser.addArgument("config").nargs(1)
				.help("configuration file");
        Namespace ns = null;
        
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        AppArgs ret = new AppArgs();
        
        ret.setInputFile(this.formatFileName(ns.getString("input")
									.replaceAll("\\[", "").replaceAll("\\]", "")));
        ret.setOutputFile(this.formatFileName(ns.getString("output")
									.replaceAll("\\[", "").replaceAll("\\]", "")));
        ret.setConfigFile(this.formatFileName(ns.getString("config")
        							.replaceAll("\\[", "").replaceAll("\\]", "")));
        ret.setSimilarity(SimilarityType.valueOf(ns.getString("similarity")));
        ret.setComputeDistances(Boolean.parseBoolean(ns.getString("compute_distances")));
        ret.setThreads(Integer.parseInt(ns.getString("threads")));

        Logger.log(Type.INFO, "Input file:        " + ret.getInputFile());
        Logger.log(Type.INFO, "Output file:       " + ret.getOutputFile());
        Logger.log(Type.INFO, "Config file:       " + ret.getConfigFile());
        Logger.log(Type.INFO, "Similarity:        " + ret.getSimilarity());
        Logger.log(Type.INFO, "Compute distances: " + ret.getComputeDistances());
        Logger.log(Type.INFO, "Threads:           " + ret.getThreads());
        
        return ret;
	}

    private String formatFileName(String file) {
		String curDir = System.getProperty("user.dir");
		boolean backslash = curDir.contains("/") ? false : true;
		
		if(!file.contains("\\") && !file.contains("/")) {
			return backslash ? curDir + "\\" + file : curDir + "/" + file;
		}
		
		return file;
    }

}
