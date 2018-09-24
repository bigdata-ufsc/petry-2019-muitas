package br.com.lucasmaypetry.base.config;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppArgs {

	private Boolean distances;

	private List<String> inputFiles;
	
	private String inputExtension;
	
	private String outputFile;
	
	private String classFile;
	
	private List<EvaluationMetric> metrics;

}
