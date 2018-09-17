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

	@JsonProperty("attributes")
	private Map<String, AttributeConfiguration> attributes;

}
