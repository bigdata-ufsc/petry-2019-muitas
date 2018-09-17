package br.com.lucasmaypetry.distance;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import br.com.lucasmaypetry.base.Attribute;

public class ExpressionDistanceFunction implements DistanceFunction<Attribute> {

	private Argument x;
	private Argument y;
	private Expression e;
	
	public ExpressionDistanceFunction(String expression) {
		this.x = new Argument("x = 0");
		this.y = new Argument("y = 0");
		this.e = new Expression(expression, x, y);
	}
	
	@Override
	public double distance(Attribute o1, Attribute o2) {
		this.x.setArgumentValue((double) o1.getValue());
		this.y.setArgumentValue((double) o2.getValue());
		return e.calculate();
	}

}
