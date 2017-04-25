package graph.graphicsObjects;

import java.awt.Graphics;
import java.awt.Color;

public class Edge{
	//declares variables for use in the program
	private Color edgeCol;
	private Point pointOne;
	private Point pointTwo;
	private String text;
	private boolean arc;
	
	//edge constructor
	public Edge(Color edgeCol, Point pointOne, Point pointTwo,String text, boolean arc){
		//stores the edges colour
		this.edgeCol=edgeCol;
		//stores the end point coords of the edge
		this.pointOne=pointOne;
		this.pointTwo=pointTwo;
		
		//stores the edges label
		this.text=text;
		//stores if the edge is an arc or not
		this.arc=arc;
	}
	
	//graph method for the edge
	public void draw(Graphics g){
		//check if the edge is an arc
		if(arc){
			//gets the end point coordinates
			int pointOneX=pointOne.getXCoord();
			int pointOneY=pointOne.getYCoord();
			
			int pointTwoX=pointTwo.getXCoord();
			int pointTwoY=pointTwo.getYCoord();
			
			//checks if the end points are below each other
			if(pointOneY==pointTwoY){
				//makes it so the pointOne occurs before pointTow
				if(pointOneX>pointTwoX){
					int temp=pointOneX;
					pointOneX=pointTwoX;
					pointTwoX=temp;
					
					temp=pointOneY;
					pointOneY=pointTwoY;
					pointTwoY=temp;
				}
				
				
				//draws the arc
				g.setColor(edgeCol);
				g.drawArc(pointOneX, pointOneY-25, pointTwoX-pointOneX, 50, 180, 180);
				
				//draws the label
				g.setColor(Color.black);
				int textXCoord=(pointOneX + pointTwoX)/2;
				int textYCoord=(pointOneY + pointTwoY)/2 + 20;
				
				g.drawString(text, textXCoord, textYCoord);
			}
			//ran if the end points have the same x coord
			else{
				//makes it so pointOne comes before pointTwo
				if(pointOneY>pointTwoY){
					int temp=pointOneX;
					pointOneX=pointTwoX;
					pointTwoX=temp;
					
					temp=pointOneY;
					pointOneY=pointTwoY;
					pointTwoY=temp;
				}
				
				//draws the arc
				g.setColor(edgeCol);
				g.drawArc(pointOneX-25, pointOneY, 50, pointTwoY-pointOneY, 270, 180);
				
				//draws the label
				g.setColor(Color.black);
				int textXCoord=(pointOneX + pointTwoX)/2+5;
				int textYCoord=(pointOneY + pointTwoY)/2;
				
				g.drawString(text, textXCoord, textYCoord);
			}
			
		}
		//otherwise it draws a straight line
		else{
			//gest the coords
			int pointOneX=pointOne.getXCoord();
			int pointOneY=pointOne.getYCoord();
			
			int pointTwoX=pointTwo.getXCoord();
			int pointTwoY=pointTwo.getYCoord();
			
			//draws the line
			g.setColor(edgeCol);
			g.drawLine(pointOneX, pointOneY, pointTwoX, pointTwoY);
			
			//draws the text
			g.setColor(Color.black);
			int textXCoord=(pointOneX+pointTwoX)/2;			
			int textYCoord=(pointOneY + pointTwoY)/2;			
			
			//if the line is a diagonal then we move the text slightly towards one of the end points, to reduce
			//text been drawn on top of each other
			if(pointOneX!=pointTwoX && pointOneY!=pointTwoY){
				textXCoord=textXCoord + ((pointOneX-textXCoord)*3)/20;
				textYCoord=textYCoord + ((pointOneY-textYCoord)*3)/20;
			}
			
			g.drawString(text, textXCoord, textYCoord);
		}
	}
}