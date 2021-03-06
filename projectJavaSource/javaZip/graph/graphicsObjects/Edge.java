package graph.graphicsObjects;

import java.awt.Graphics;
import java.awt.Color;

public class Edge{
	private Color edgeCol;
	private Point pointOne;
	private Point pointTwo;
	private String text;
	private boolean arc;
	
	public Edge(Color edgeCol, Point pointOne, Point pointTwo,String text, boolean arc){
		this.edgeCol=edgeCol;
		this.pointOne=pointOne;
		this.pointTwo=pointTwo;
		this.text=text;
		this.arc=arc;
	}
	
	public void draw(Graphics g){
		
		if(arc){
			int pointOneX=pointOne.getXCoord();
			int pointOneY=pointOne.getYCoord();
			
			int pointTwoX=pointTwo.getXCoord();
			int pointTwoY=pointTwo.getYCoord();
			
			if(pointOneY==pointTwoY){
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
				
				g.setColor(Color.black);
				int textXCoord=(pointOneX + pointTwoX)/2;
				int textYCoord=(pointOneY + pointTwoY)/2 + 20;
				
				g.drawString(text, textXCoord, textYCoord);
			}
			else{
				if(pointOneY>pointTwoY){
					int temp=pointOneX;
					pointOneX=pointTwoX;
					pointTwoX=temp;
					
					temp=pointOneY;
					pointOneY=pointTwoY;
					pointTwoY=temp;
				}
				
				g.setColor(edgeCol);
				g.drawArc(pointOneX-25, pointOneY, 50, pointTwoY-pointOneY, 270, 180);
				
				g.setColor(Color.black);
				int textXCoord=(pointOneX + pointTwoX)/2+5;
				int textYCoord=(pointOneY + pointTwoY)/2;
				
				g.drawString(text, textXCoord, textYCoord);
			}
			
		}
		else{
			int pointOneX=pointOne.getXCoord();
			int pointOneY=pointOne.getYCoord();
			
			int pointTwoX=pointTwo.getXCoord();
			int pointTwoY=pointTwo.getYCoord();
			
			//draws the line
			g.setColor(edgeCol);
			g.drawLine(pointOneX, pointOneY, pointTwoX, pointTwoY);
			
			g.setColor(Color.black);
			int textXCoord=(pointOneX+pointTwoX)/2;			
			int textYCoord=(pointOneY + pointTwoY)/2;			
			
			if(pointOneX!=pointTwoX && pointOneY!=pointTwoY){
				textXCoord=textXCoord + ((pointOneX-textXCoord)*3)/20;
				textYCoord=textYCoord + ((pointOneY-textYCoord)*3)/20;
			}
			
			g.drawString(text, textXCoord, textYCoord);
		}
	}
}