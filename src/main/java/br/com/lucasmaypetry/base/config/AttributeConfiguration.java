package br.com.lucasmaypetry.base.config;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttributeConfiguration {

	@JsonProperty("type")
	private DataType type;

	@JsonProperty("distance_function")
	private String distance_function;

	@JsonProperty("weight")
	private double weight;

	@JsonProperty("threshold")
	private List<Double> thresholds;

}
