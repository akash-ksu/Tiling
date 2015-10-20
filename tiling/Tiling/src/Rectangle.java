public class Rectangle{
	private Point topLeft;
	private Point bottomRight;
	private Point topRight;
	private Point bottomLeft;
	public Point getTopRight() {
		return topRight;
	}
	public void setTopRight(Point topRight) {
		this.topRight = topRight;
	}
	public Point getBottomLeft() {
		return bottomLeft;
	}
	public void setBottomLeft(Point bottomLeft) {
		this.bottomLeft = bottomLeft;
	}
	public Point getTopLeft() {
		return topLeft;
	}
	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}
	public Point getBottomRight() {
		return bottomRight;
	}
	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}	
	Rectangle(final Point topLeft, final Point bottomLeft, final Point topRight,Point bottomRight){
		this.topLeft = topLeft;
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
		this.bottomRight = bottomRight;
	}

}