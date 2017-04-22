package graph.graphicsObjects;

public class Point{
	//set variables for use in the class
	private int xCoord;
	private int yCoord;
	
	//edge constructor
	public Point(int xCoord, int yCoord){
		this.xCoord=xCoord;
		this.yCoord=yCoord;
	}
	
	//method to set the coordinates for the point
	public void setCoords(int xCoord, int yCoord){
		this.xCoord=xCoord;
		this.yCoord=yCoord;
	}
	
	//gets the x coordinate
	public int getXCoord(){
		return xCoord;
	}
	
	//gets the y coordinate
	public int getYCoord(){
		return yCoord;
	}
}