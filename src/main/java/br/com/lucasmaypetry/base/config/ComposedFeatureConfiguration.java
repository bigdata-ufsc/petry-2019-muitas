package br.com.lucasmaypetry.base.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComposedFeatureConfiguration {

	@JsonProperty("features")
	private List<String> features;

	@JsonProperty("weight")
	private double weight;

}
