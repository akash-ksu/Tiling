
/*
 * The equation is of the form
 * ax + by + c = 0
 * */

public class LinearEquation {
	private double xConstant;
	private double yConstant;
	private double constant;
	// Create LinearEquation using the coefficients of x,y and constant.
	public LinearEquation(double xConstant, double yConstant, double constant){
		this.xConstant = xConstant;
		this.yConstant = yConstant;
		this.constant = constant;
	}

	public Double getxConstant() {
		return xConstant;
	}

	public void setxConstant(Double xConstant) {
		this.xConstant = xConstant;
	}

	public Double getyConstant() {
		return yConstant;
	}

	public void setyConstant(Double yConstant) {
		this.yConstant = yConstant;
	}

	public Double getConstant() {
		return constant;
	}

	public void setConstant(Double constant) {
		this.constant = constant;
	}
	
	//create a LinearEquation using two points
	public LinearEquation(Point firstPoint, Point secondPoint){
		double slope = 0.0;
		if((secondPoint.getX() - firstPoint.getX()) != 0){
			slope = (secondPoint.getY() - firstPoint.getY()) / (secondPoint.getX() - firstPoint.getX());
			this.xConstant = slope;
			this.yConstant = -1;
			this.constant = secondPoint.getY() - slope*secondPoint.getX();
		} else if((secondPoint.getX() - firstPoint.getX()) == 0) {
			this.xConstant = 1.0;
			this.yConstant = 0.0;
			this.constant = - secondPoint.getX();
		}
	}
}
