
public class Point {
	private double x;
	private double y;
	
	public Point(Double x, Double y){
		this.x = x;
		this.y = y;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(obj instanceof Point){
			if(this.x == ((Point)obj).x && this.y == ((Point)obj).y){
				return true;
			}
		}
		
		return false;	
	}
}
