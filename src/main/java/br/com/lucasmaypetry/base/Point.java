package br.com.lucasmaypetry.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

public class Point {

	@Getter
	private Map<Integer, Attribute> attributes;

	public Point(List<Attribute> attributes) {
		this.attributes = new HashMap<Integer, Attribute>();

		for (Attribute attribute : attributes) {
			this.attributes.put(attribute.getType(), attribute);
		}
	}

	public Point(Attribute... attributes) {
		this.attributes = new HashMap<Integer, Attribute>();

		for (Attribute attribute : attributes) {
			this.attributes.put(attribute.getType(), attribute);
		}
	}

	public Attribute getAttributeByType(int type) {
		return this.attributes.get(type);
	}

	public List<Attribute> getAttributesAsList() {
		List<Attribute> sortedList = this.attributes.values().stream().collect(Collectors.toList());
		Collections.sort(sortedList, (o1, o2) -> o1.getType() > o2.getType() ? 1 : -1);

		return sortedList;
	}

	@Override
	public Point clone() {
		Attribute[] newAttr = new Attribute[this.attributes.size()];
		int i = 0;

		for (Attribute a : this.attributes.values())
			newAttr[i++] = a.clone();

		return new Point(newAttr);
	}

}
