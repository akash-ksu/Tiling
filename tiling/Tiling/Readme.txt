README:

Java program to lay rectangles along the first edge of a closed polygon of arbitrary sides

Assumptions:
1.	The polygon is a regular convex polygon with n >= 3
2.	User provides the input as a json file in a specified format. All the test files are placed in the test_data folder.
3.	The user specifies the length and the breadth of the rectangles to draw. The smaller of the two is used along the first edge.

NOTE: In order to use different input json. You need to change the name in the main method of TitlesPlacement.java
	  You might also need to change the build path for the jar. The jars needed are pakcged in the zip folder

Classes:

1.	TilesPlacement.java : This class contains the main method for the program. It prints out all the rectangles that are placed along the first edge on the console. If both the length and the breadth of the rectangle provided is greater than the length of  the first side no rectangle can be placed. It also considers both the cases when n = 3 (triangle) and n> 3 (any other polygon).
2.	Rectangle.java : A class to represent the rectangle
3.	Point.java : A class to represent a point in the coordinate system.
4.	LinearEquation.java : This class is used to represent a linear equation for a line.
5.	Utility.java : This class contains all the utility methods that are needed for the program. 

	



