package br.com.lucasmaypetry.base.config;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExperimentConfiguration {

	@JsonProperty("experiment_name")
	private String experimentName;

	@JsonProperty("tid_col")
	private String tid;

	@JsonProperty("label_col")
	private String label;

	@JsonProperty("similarity")
	private SimilarityType similarity;
	
	@JsonProperty("compute_distances")
	private Boolean computeDistances;

	@JsonProperty("threads")
	private int threads;

	@JsonProperty("features")
	private Map<String, FeatureConfiguration> features;

}
