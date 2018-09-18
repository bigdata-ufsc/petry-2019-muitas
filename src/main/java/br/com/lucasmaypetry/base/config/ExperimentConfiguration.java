package br.com.lucasmaypetry.base.config;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
	
	@JsonProperty("features_analysis")
	private List<ComposedFeatureConfiguration> featuresAnalysis;

}
