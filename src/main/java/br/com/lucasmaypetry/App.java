package br.com.lucasmaypetry;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lucasmaypetry.base.config.AppArgs;
import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.experiment.ExperimentRunner;
import br.com.lucasmaypetry.utils.ArgParser;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;

public class App {
	
    public static void main(String[] args) {
    	AppArgs procArgs = new ArgParser().parse(args);
		ExperimentConfiguration config = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			config = mapper.readValue(new File(procArgs.getConfigFile()), ExperimentConfiguration.class);
		} catch (IOException e) {
			Logger.log(Type.ERROR, e.getMessage());
			e.printStackTrace();
		}

		new ExperimentRunner(config, procArgs).run();
    }
    
}
