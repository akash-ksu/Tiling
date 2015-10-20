import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import com.google.common.base.Preconditions;

/**
 * This class contains utility methods for the program.
 */
public class Utility {	
	private static double xDirection = 0.0;
	private static double yDirection = 0.0;
	private static List<Rectangle> rectangleList = new ArrayList<>();
	private static double theta;
	private static double gamma;
	
	// A function to get the intersection of two lines.The equation should be in the form of ax + by = c.
	public static Point intersectionPoint(LinearEquation firstLine, LinearEquation secondLine){
		Preconditions.checkNotNull(firstLine, "firstLine cannot be null");
		Preconditions.checkNotNull(secondLine, "secondLine cannot be null");
		final RealMatrix coefficients = new Array2DRowRealMatrix(new double[][] {{firstLine.getxConstant(),firstLine.getyConstant()},{secondLine.getxConstant(),secondLine.getyConstant()}}, false);
		final DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
		
		final RealVector constants = new ArrayRealVector(new double[]{-firstLine.getConstant(),-secondLine.getConstant()}, false);
		final RealVector solution = solver.solve(constants);		
		return new Point(solution.getEntry(0), solution.getEntry(1));
	}
	
	// function to find the line parallel to a given line at a particular distance which intersect the polygon
	public static LinearEquation correctParallelLine(final Point[] polygon, final double length){
		Preconditions.checkNotNull(polygon, "polygon cannot be null");
		final LinearEquation firstLineEquation = new LinearEquation(polygon[0], polygon[1]);
		final LinearEquation[] parallelLines = findParallelLines(firstLineEquation, length);
		if(polygon.length>3){			
			final Point perFoot1 = perpendicularFoot(polygon[0], parallelLines[0]);
			final Point perFoot2 = perpendicularFoot(polygon[0], parallelLines[1]);
			if(contains(polygon, perFoot1)){
				return parallelLines[0];
			}else if(contains(polygon, perFoot2)){
				return parallelLines[1];
			} else {
				return null;
			}
		}else if(polygon.length == 3) {
			// It is a triangle
			final LinearEquation adjacentLineThroughFirstPoint = new LinearEquation(polygon[0],polygon[polygon.length-1]);
			final Point intersectionPoint1 = Utility.intersectionPoint(adjacentLineThroughFirstPoint, parallelLines[0]);
			final Point intersectionPoint2 = Utility.intersectionPoint(adjacentLineThroughFirstPoint, parallelLines[1]);
			if(contains(polygon, intersectionPoint1)){
				return parallelLines[0];
			}else if(contains(polygon, intersectionPoint2)){
				return parallelLines[1];
			} else {
				return null;
			}
		}
		return null;
	}
	
	// To get new point using parametric form
	public static Point nextPoint(final Point firstPoint,final double length, final double theta) {
		Preconditions.checkNotNull(firstPoint, "firstPoint cannot be null");
		final double x = firstPoint.getX() + xDirection*length*Math.cos(theta);
		final double y = firstPoint.getY() + yDirection*length*Math.sin(theta);
		return new Point(x,y);
	}
	
	// To set the direction
	public static void setDirection(final Point firstPoint,final Point secondPoint) {
		xDirection =  Math.signum(secondPoint.getX()-firstPoint.getX());
		yDirection =  Math.signum(secondPoint.getY()-firstPoint.getY());
	}
	
	// To get List of Rectangles that can fit inside a polygon
	public static List<Rectangle> getRectangles(Point firstPolygonPoint, Point firstPerpendicularPoint,final LinearEquation parallelLine, final double breadth, final double length, final Point[] polygon) {
		Preconditions.checkNotNull(firstPolygonPoint, "firstPolygonPoint cannot be null");
		Preconditions.checkNotNull(firstPerpendicularPoint, "firstPerpendicularPoint cannot be null");
		Preconditions.checkNotNull(parallelLine, "parallelLine cannot be null");
		Preconditions.checkNotNull(polygon, "polygon cannot be null");
		List<Point> rectanglePoints = new ArrayList<Point>();
		theta =  Math.abs(Math.atan(- parallelLine.getxConstant()/parallelLine.getyConstant()));
		gamma = Math.abs(theta - Math.PI/2);
		Utility.setDirection(polygon[0], polygon[1]);
		while(true){
			Point nextPolygonPoint = Utility.nextPoint(firstPolygonPoint, breadth, theta);
			Point nextPerpendicularPoint = Utility.nextPoint(firstPerpendicularPoint, breadth, theta);		
			if(Utility.contains(polygon, nextPerpendicularPoint) && Utility.contains(polygon, nextPolygonPoint)){
				Rectangle rectangleCordinates = new Rectangle(firstPolygonPoint,nextPolygonPoint,firstPerpendicularPoint,nextPerpendicularPoint);
				rectangleList.add(rectangleCordinates);
				if (!rectanglePoints.contains(firstPerpendicularPoint)){
					rectanglePoints.add(firstPerpendicularPoint);
				}
				if (!rectanglePoints.contains(nextPerpendicularPoint)){
					rectanglePoints.add(nextPerpendicularPoint);
				}
			}else{
				break;
			}
			firstPolygonPoint = nextPolygonPoint;
			firstPerpendicularPoint = nextPerpendicularPoint;
		}
//		Utility.setDirection(rectangleList.get(0).getTopLeft(), rectangleList.get(0).getTopRight());
//		do{
//			List<Point> newRectanglePoints = new ArrayList<Point>();
//			for(int i=0; i < rectanglePoints.size()-1; i++){
//				Point firstcorner = Utility.nextPoint(rectanglePoints.get(i), length, gamma);
//				Point secondcorner = Utility.nextPoint(rectanglePoints.get(i+1), length, gamma);
//				if(Utility.contains(polygon, rectanglePoints.get(i)) && Utility.contains(polygon, rectanglePoints.get(i+1)) && Utility.contains(polygon, firstcorner) && Utility.contains(polygon, secondcorner)){
//					Rectangle newRectangle = new Rectangle(rectanglePoints.get(i),rectanglePoints.get(i+1),firstcorner,secondcorner);
//					rectangleList.add(newRectangle);
//					if (!newRectanglePoints.contains(firstcorner)){
//						newRectanglePoints.add(firstcorner);
//					}
//					if (!newRectanglePoints.contains(secondcorner)){
//						newRectanglePoints.add(secondcorner);
//					}
//				}
//			}
//			rectanglePoints.clear();
//			rectanglePoints.addAll(newRectanglePoints);			
//		} while(rectanglePoints.size()>0);	
		for(Rectangle rect: rectangleList){
			getSurrondingRectangles(rect, polygon, length);
		}
		return rectangleList;
	}	
	
	// Find all the surrounding valid rectangles around a rectangle
	public static void getSurrondingRectangles(Rectangle rectangle, Point[] polygon, double length){
		List<Rectangle> surrondingRectangles = new ArrayList<>();
		double rectLeftXDir = Math.signum(rectangle.getTopRight().getX() - rectangle.getTopLeft().getX());
		double rectLeftYDir = Math.signum(rectangle.getTopRight().getY() - rectangle.getTopLeft().getY());
		Point leftRectTopNew = new Point(rectangle.getTopLeft().getX()+rectLeftXDir*length*Math.cos(gamma),rectangle.getTopLeft().getY()+rectLeftYDir*length*Math.sin(gamma));
		Point lefRecttBottomNew = new Point(rectangle.getBottomLeft().getX()+rectLeftXDir*length*Math.cos(gamma),rectangle.getBottomLeft().getY()+rectLeftYDir*length*Math.sin(gamma));
		Rectangle leftRect = new Rectangle(leftRectTopNew, lefRecttBottomNew, rectangle.getTopLeft(), rectangle.getBottomLeft());
		
		double rectTopXDir = Math.signum(rectangle.getBottomLeft().getX() - rectangle.getTopLeft().getX());
		double rectTopYDir = Math.signum(rectangle.getBottomLeft().getY() - rectangle.getTopLeft().getY());
		Point topRectTopNew = new Point(rectangle.getTopLeft().getX()+rectTopXDir*length*Math.cos(theta),rectangle.getTopLeft().getY()+rectTopYDir*length*Math.sin(theta));
		Point topRecttRigthNew = new Point(rectangle.getTopRight().getX()+rectTopXDir*length*Math.cos(theta),rectangle.getTopRight().getY()+rectTopYDir*length*Math.sin(theta));
		Rectangle topRect = new Rectangle(topRectTopNew, rectangle.getTopLeft(), topRecttRigthNew, rectangle.getTopRight());
		
		double rectRightXDir = Math.signum(rectangle.getTopLeft().getX() - rectangle.getTopRight().getX());
		double rectRightYDir = Math.signum(rectangle.getTopLeft().getY() - rectangle.getTopRight().getY());
		Point rightRectRightNew = new Point(rectangle.getTopRight().getX()+rectRightXDir*length*Math.cos(gamma),rectangle.getTopRight().getY()+rectRightYDir*length*Math.sin(gamma));
		Point rightRecttBottomNew = new Point(rectangle.getBottomRight().getX()+rectRightXDir*length*Math.cos(gamma),rectangle.getBottomRight().getY()+rectRightYDir*length*Math.sin(gamma));
		Rectangle rightRect = new Rectangle(rectangle.getTopRight(), rectangle.getBottomRight(), rightRectRightNew, rightRecttBottomNew);
		
		double rectDownXDir = Math.signum(rectangle.getTopLeft().getX() - rectangle.getBottomLeft().getX());
		double rectDownYDir = Math.signum(rectangle.getTopLeft().getY() - rectangle.getBottomLeft().getY());
		Point downRectLeftNew = new Point(rectangle.getBottomLeft().getX()+rectDownXDir*length*Math.cos(theta),rectangle.getBottomLeft().getY()+rectDownYDir*length*Math.sin(theta));
		Point downRecttRightNew = new Point(rectangle.getBottomRight().getX()+rectDownXDir*length*Math.cos(theta),rectangle.getBottomRight().getY()+rectDownYDir*length*Math.sin(theta));
		Rectangle downRect = new Rectangle(rectangle.getBottomLeft(), downRectLeftNew, rectangle.getBottomRight(), downRecttRightNew);
		
		if(contains(polygon, leftRect)){
			if(!rectangleList.contains(leftRect)){
				rectangleList.add(leftRect);
				getSurrondingRectangles(leftRect, polygon, length);
			}
		}
		
		if(contains(polygon, rightRect)){
			if(!rectangleList.contains(rightRect)){
				rectangleList.add(rightRect);
				getSurrondingRectangles(rightRect, polygon, length);
			}
		}
		
		if(contains(polygon, topRect)){
			if(!rectangleList.contains(topRect)){
				rectangleList.add(topRect);
				getSurrondingRectangles(topRect, polygon, length);
			}
		}
		
		if(contains(polygon, downRect)){
			if(!rectangleList.contains(downRect)){
				rectangleList.add(downRect);
				getSurrondingRectangles(downRect, polygon, length);
			}
		}
	}
		
	// To find the foot of perpendicular 
	public static Point perpendicularFoot(Point firstPoint, LinearEquation line){
		Preconditions.checkNotNull(firstPoint, "firstPoint cannot be null");
		Preconditions.checkNotNull(line, "line cannot be null");
		final double p = firstPoint.getX();
		final double q = firstPoint.getY();
		final double a = line.getxConstant();
		final double b = line.getyConstant();
		final double c = line.getConstant();
		final double h = p- a*(a*p+b*q+c)/(pow(a, 2) +pow(b, 2));
		final double k = q - b*(a*p+b*q+c)/(pow(a, 2) +pow(b, 2));
		return new Point(h, k);
	}
	
	//To find parallel lines at a distance to a given line
	public static LinearEquation[] findParallelLines(final LinearEquation line, final double length){
		Preconditions.checkNotNull(line, "line cannot be null");
		final LinearEquation[] parallelLines = new LinearEquation[2];
		if(line.getyConstant() != 0){
		final double slope = - line.getxConstant()/line.getyConstant();
		parallelLines[0] = new LinearEquation(-slope, 1, -line.getConstant()+length*sqrt(pow(slope, 2)+1));
		parallelLines[1] = new LinearEquation(-slope, 1, -line.getConstant()-length*sqrt(pow(slope, 2)+1));
		} else {
			parallelLines[0] = new LinearEquation(1, 0, (line.getConstant()+length));
			parallelLines[1] = new LinearEquation(1, 0, (line.getConstant()-length));
		}
		return parallelLines;		
	}
	
	// To find the distance between two points
	public static double distance(final double x1,final double y1,final double x2,final double y2){
		return sqrt(pow((x2 - x1), 2) + pow((y2-y1), 2));
	}
	
	// To find the distance between two points
		public static double distance(Point p1,Point p2){
			return sqrt(pow((p2.getX() - p1.getX()), 2) + pow((p2.getY()-p1.getY()), 2));
		}
	
	
	public static boolean contains(Point[] polygon, Rectangle rect){
		boolean result = false;
		if(contains(polygon, rect.getTopLeft()) && contains(polygon, rect.getBottomLeft()) && contains(polygon, rect.getTopRight()) && contains(polygon, rect.getBottomRight())){
			result = true;
		}
		return result;
	}	
	// To check if a point lies within the polygon
	public static boolean contains(Point[] polygon, Point point) {
		Preconditions.checkNotNull(polygon,"polygon cannot be null");
		Preconditions.checkNotNull(point,"point cannot be null");
        int i, j;
        boolean result = false;
       if(polygon.length>3){
    	   //Check if the point is inside the polygon
	        for(i=0, j = polygon.length - 1; i<polygon.length; j = i++) {
	            if((polygon[i].getY() > point.getY()) != (polygon[j].getY() > point.getY()) &&
	                    (point.getX() < (polygon[j].getX() - polygon[i].getX())*(point.getY() - polygon[i].getY())/(polygon[j].getY()-polygon[i].getY()) + polygon[i].getX())) {
	                result = !result;
	            }
	        }
	        
	     //Check if the point is on the polygon
	        for(i=0;i<polygon.length-1;i++){
	        	LinearEquation line = new LinearEquation(polygon[i], polygon[i+1]);
	        	if((distance(polygon[i], point)+ distance(polygon[i+1], point) == distance(polygon[i], polygon[i+1])) && line.getxConstant()*point.getX()+line.getyConstant()*point.getY()+line.getConstant() == 0){
	        		result = true;
	        		break;
	        	}
	        }
	        if(i == polygon.length){
	        	LinearEquation line = new LinearEquation(polygon[polygon.length-1], polygon[0]);
	        	if((distance(polygon[polygon.length-1], point)+ distance(polygon[0], point) == distance(polygon[0], polygon[polygon.length-1])) && line.getxConstant()*point.getX()+line.getyConstant()*point.getY()+line.getConstant() == 0){
	        		result = true;
	        	}
	        }
       } else if(polygon.length == 3) {
    	   final double epsilon = 0.0001;
    	   final double A = area (polygon[0], polygon[1], polygon[2]);
       	   final double A1 = area (point,polygon[1],polygon[2]);
       	   final double A2 = area (polygon[0], point, polygon[2]);
       	   final double A3 = area (polygon[0],polygon[1], point); 	   
    	   /* Check if sum of A1, A2 and A3 is same as A */
       	   result = Math.abs(A - (A1+A2+A3))<epsilon;
       }
        return result;
    }	
	
	public static double area(final Point point1,final Point point2, final Point point3)
	{
		Preconditions.checkNotNull(point1,"point1 cannot be null");
		Preconditions.checkNotNull(point2,"point2 cannot be null");
		Preconditions.checkNotNull(point3,"point3 cannot be null");
	   return abs((point1.getX()*(point2.getY()-point3.getY()) + point2.getX()*(point3.getY()-point1.getY())+ point3.getX()*(point1.getY()-point2.getY()))/2.0);
	}
}
