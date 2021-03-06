package br.com.lucasmaypetry.base.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeatureConfiguration {

	@JsonProperty("type")
	private DataType type;

	@JsonProperty("distance_function")
	private String distanceFunction;

	@JsonProperty("format")
	private String format;

	@JsonProperty("weight")
	private double weight;

	@JsonProperty("threshold")
	private List<Double> thresholds;

}
