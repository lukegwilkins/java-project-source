package graphicsObjects;

import java.awt.Graphics;
import java.awt.Color;

public class Circle{
	private Color col;
	private int xCoord;
	private int yCoord;
	private int diameter;
	private String text;
	
	public Circle(Color col, int xCoord, int yCoord, int diameter,String text){
		this.col=col;
		this.xCoord=xCoord;
		this.yCoord=yCoord;
		this.diameter=diameter;
		this.text=text;
	}
	
	public void draw(Graphics g){
		//draws circle
		g.setColor(col);
		g.fillOval(xCoord-diameter/2, yCoord-diameter/2, diameter, diameter);
		
		//bounding box for the circle
		g.drawRect(xCoord-diameter/2, yCoord-diameter/2, diameter, diameter);
		
		//draws text on circle
		g.setColor(Color.black);
		g.drawString(text, xCoord-3, yCoord+3);
	}
}