package br.com.lucasmaypetry.exception;

import br.com.lucasmaypetry.base.Feature;

public class IncompatibleAttributeTypesException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncompatibleAttributeTypesException(Feature a1, Feature a2) {
		super("The attributes " + a1.getName() + " (type: " + a1.getType() + ") and " + a2.getName() + "(type: " + a2.getType() + ") are not compatible!");
	}

}
