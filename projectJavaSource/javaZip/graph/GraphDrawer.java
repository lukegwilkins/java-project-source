import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import graphicsObjects.*;

public class GraphDrawer extends JFrame{
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private Edge edgeOne;
	private int height;
	private int width;
	
	private Node test;
	
	private Point nextPoint;
	public GraphDrawer(){
		height=600;
		width=600;
		
		setTitle("Graph");
		setSize(width, height);
		//setLocation(100,100);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		nextPoint=new Point(50,50);
		nodes= new ArrayList<Node>();
		edges= new ArrayList<Edge>();
		//addNode(Color.green,"A");
		
		//test = new Node(Color.green, nextPoint, 20, "A");
	}
	
	@Override
	public void paint(Graphics g){
		//we translate the origin down by 22 pixels to offset the border of the frame
		g.translate(0,22);
		//getContentPane().setBackground(Color.GREEN);
		//set the background colour
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		for(Edge edge:edges){
			edge.draw(g);
		}
		
		for(Node node:nodes){
			node.draw(g);
		}
		/*g.setColor(Color.RED);
		g.fillOval(480, 480, 200, 200);*/
		//nodeOne.draw(g);
		//test.draw(g);
	}
	
	public void addNode(Color col, String text){
		Node node = new Node(col, nextPoint, 30,text);
		
		int xCoord = nextPoint.getXCoord();
		int yCoord = nextPoint.getYCoord();
		
		xCoord=xCoord+width/5;
		if(xCoord>width-20){
			xCoord=50;
			yCoord=yCoord+height/5;
		}
		nextPoint = new Point(xCoord,yCoord);
		nodes.add(node);
		repaint();
	}
	
	public void addEdge(Color col, String textIdOne,String textIdTwo, String label){
		Node nodeOne = null;
		Node nodeTwo = null;
		
		int i=0;
		while((nodeOne==null || nodeTwo==null) && i<nodes.size()){
			if(nodes.get(i).getText().equals(textIdOne)){
				nodeOne=nodes.get(i);
			}
			
			else if(nodes.get(i).getText().equals(textIdTwo)){
				nodeTwo=nodes.get(i);
			}
			i++;
		}
		
		Edge edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,true);
		
		edges.add(edge);
		repaint();
	}
	public static void main(String[] args){
		
		java.awt.EventQueue.invokeLater(new Runnable(){
				public void run(){
					GraphDrawer graphDrawer = new GraphDrawer();
					
					graphDrawer.addNode(Color.green,"A");
					graphDrawer.addNode(Color.blue,"B");
					
					graphDrawer.addNode(Color.red,"C");
					graphDrawer.addNode(Color.yellow,"D");
					graphDrawer.addNode(Color.pink,"E");
					
					graphDrawer.addNode(Color.magenta,"F");
					
					graphDrawer.addEdge(Color.lightGray, "B", "A", "20");
				}
			}
		);
		
		
		
		
		//c.paint(null);
	}
}