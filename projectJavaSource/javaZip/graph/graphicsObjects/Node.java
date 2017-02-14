package graphicsObjects;

import java.awt.Graphics;
import java.awt.Color;

public class Node{
	private Color col;
	private Point coords;
	private int diameter;
	private String text;
	
	public Node(Color col, Point coords, int diameter,String text){
		this.col=col;
		this.coords=coords;
		this.diameter=diameter;
		this.text=text;
	}
	
	public void draw(Graphics g){
		
		int xCoord = coords.getXCoord();
		int yCoord = coords.getYCoord();
		
		//draws circle
		g.setColor(col);
		//figure out cordinates to have center are xcoord and y coord
		g.fillOval(xCoord-diameter/2, yCoord-diameter/2, diameter, diameter);
		
		//bounding box for the circle
		//g.drawRect(xCoord-diameter/2, yCoord-diameter/2, diameter, diameter);
		
		//draws text on circle
		g.setColor(Color.black);
		g.drawString(text, xCoord-3, yCoord+3);
	}
	
	public Point getCoords(){
		return coords;
	}
	
	public String getText(){
		return text;
	}
}