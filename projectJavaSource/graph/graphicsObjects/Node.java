package graph.graphicsObjects;

import java.awt.Graphics;
import java.awt.Color;

public class Node{
	//sets up variables for use in the class
	private Color col;
	private Point coords;
	private int diameter;
	private String text;
	
	//node constructor
	public Node(Color col, Point coords, int diameter,String text){
		//stores the node's colour, coordinates, diamater and text
		this.col=col;
		this.coords=coords;
		this.diameter=diameter;
		this.text=text;
	}
	
	//draw method for the node
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
	
	//gets the nodes coordinates
	public Point getCoords(){
		return coords;
	}
	
	//gets the nodes String
	public String getText(){
		return text;
	}
}