package br.com.lucasmaypetry.base.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import br.com.lucasmaypetry.base.Application;
import br.com.lucasmaypetry.similarity.SimilarityMeasure;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SimilarityType {

	EDR(br.com.lucasmaypetry.similarity.EDR.class),
	LCSS(br.com.lucasmaypetry.similarity.LCSS.class),
	MSM(br.com.lucasmaypetry.similarity.MSM.class);
	
	private Class<?> similarityClass;
	
	public SimilarityMeasure getMeasure(Application app) throws NoSuchMethodException,
																SecurityException,
																InstantiationException,
																IllegalAccessException,
																IllegalArgumentException,
																InvocationTargetException {
		Constructor<?> c = this.similarityClass.getDeclaredConstructor(Application.class);
		c.setAccessible(true);
		return (SimilarityMeasure) c.newInstance(app);
	}
	
}
