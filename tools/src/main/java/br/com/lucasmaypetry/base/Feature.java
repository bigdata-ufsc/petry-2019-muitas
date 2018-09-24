package br.com.lucasmaypetry.base;

import br.com.lucasmaypetry.base.config.DataType;
import br.com.lucasmaypetry.distance.DistanceFunction;
import lombok.Getter;

@Getter
public class Feature {

	private String name;
	private DataType type;
	private Object value;

	public Feature(String name, DataType type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public Feature(DataType type, Object value) {
		this.name = "";
		this.type = type;
		this.value = value;
	}

	@Override
	public Feature clone() {
		return new Feature(this.name, this.type, this.value);
	}
	
	public int matches(Feature other, DistanceFunction<Feature> distanceFunction, double threshold) {
		return distanceFunction.distance(this, other) <= threshold ? 1 : 0;
	}

}
