package br.com.lucasmaypetry.base;

import java.util.HashMap;
import java.util.Map;

import br.com.lucasmaypetry.base.config.DataType;
import br.com.lucasmaypetry.distance.DistanceFunction;
import br.com.lucasmaypetry.exception.IncompatibleAttributeTypesException;
import lombok.Getter;

@Getter
public class Feature {

	@Getter
	private static Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();

	@Getter
	private static Map<String, Integer> map = new HashMap<String, Integer>();

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
		return new Feature(this.type, this.value);
	}
	
	public int matches(Feature other, DistanceFunction<Feature> distanceFunction, double threshold) {
		if(this.getType() != other.getType()) {
			throw new IncompatibleAttributeTypesException(this, other);
		}
		
		return distanceFunction.distance(this, other) <= threshold ? 1 : 0;
	}

}
