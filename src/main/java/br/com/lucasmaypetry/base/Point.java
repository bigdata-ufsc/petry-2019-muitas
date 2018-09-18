package br.com.lucasmaypetry.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

public class Point {

	@Getter
	private Map<String, Feature> features;

	public Point(List<Feature> features) {
		this.features = new HashMap<String, Feature>();

		for (Feature feature : features) {
			this.features.put(feature.getName(), feature);
		}
	}

	public Point(Feature... features) {
		this.features = new HashMap<String, Feature>();

		for (Feature feature : features) {
			this.features.put(feature.getName(), feature);
		}
	}

	public Feature getAttribute(String type) {
		return this.features.get(type);
	}

	public List<Feature> getAttributesAsList() {
		List<Feature> sortedList = this.features.values().stream().collect(Collectors.toList());
		Collections.sort(sortedList, (o1, o2) -> o1.getName().compareTo(o2.getName()));

		return sortedList;
	}

	@Override
	public Point clone() {
		Feature[] newAttr = new Feature[this.features.size()];
		int i = 0;

		for (Feature a : this.features.values())
			newAttr[i++] = a.clone();

		return new Point(newAttr);
	}

}
