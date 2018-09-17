package br.com.lucasmaypetry.base;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Attribute {

	@Getter
	private static Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();

	@Getter
	private static Map<String, Integer> map = new HashMap<String, Integer>();

	private String name;
	private int type;
	private Object value;

	public Attribute(String name, int type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public Attribute(int type, Object value) {
		this.name = "";
		this.type = type;
		this.value = value;
	}

	@Override
	public Attribute clone() {
		return new Attribute(this.type, this.value);
	}

}
