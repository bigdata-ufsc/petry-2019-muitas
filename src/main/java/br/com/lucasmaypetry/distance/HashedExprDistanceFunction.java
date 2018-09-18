package br.com.lucasmaypetry.distance;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import br.com.lucasmaypetry.base.Feature;

public class HashedExprDistanceFunction implements DistanceFunction<Feature> {

	private Argument x;
	private Argument y;
	private Expression e;
	
	public HashedExprDistanceFunction(String expression) {
		this.x = new Argument("x = 0");
		this.y = new Argument("y = 0");
		this.e = new Expression(expression, x, y);
	}
	
	@Override
	public double distance(Feature o1, Feature o2) {
		this.x.setArgumentValue((double) o1.getValue().hashCode());
		this.y.setArgumentValue((double) o2.getValue().hashCode());
		return e.calculate();
	}

}
