package br.com.lucasmaypetry.base;

import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;

public class ExperimentRunner {

	private ExperimentConfiguration config;
	
	public ExperimentRunner(ExperimentConfiguration config) {
		this.config = config;
	}
	
	public void run() {
		Logger.log(Type.RUNNING, "Running experiment '" + config.getExperimentName() + "'... ");
		
		Logger.log(Type.RUNNING, "Running experiment '" + config.getExperimentName() + "'... DONE!");		
	}
	
}
