package br.com.lucasmaypetry.base.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppArgs {

	private SimilarityType similarity;
	
	private Boolean computeDistances;

	private String inputFile;

	private String outputFile;

	private String configFile;

	private int threads;

}
