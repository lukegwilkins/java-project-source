package graphicsObjects;

public class Point{
	private int xCoord;
	private int yCoord;
	
	public Point(int xCoord, int yCoord){
		this.xCoord=xCoord;
		this.yCoord=yCoord;
	}
	
	public void setCoords(int xCoord, int yCoord){
		this.xCoord=xCoord;
		this.yCoord=yCoord;
	}
	
	public int getXCoord(){
		return xCoord;
	}
	
	public int getYCoord(){
		return yCoord;
	}
}