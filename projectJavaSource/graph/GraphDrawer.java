package graph;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import graph.graphicsObjects.*;

public class GraphDrawer extends JFrame{
	//sets up variables for use in the program
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private Edge edgeOne;
	private int height;
	private int width;
	
	private Node test;
	
	private Point nextPoint;
	
	public GraphDrawer(){
		//sets the windows height
		height=600;
		width=600;
		
		//sets the window title
		setTitle("Graph of the Menu");
		setSize(width, height);
		//setLocation(100,100);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//sets where the first node will be drawn
		nextPoint=new Point(50,50);
		//creates a list of nodes and edges
		nodes= new ArrayList<Node>();
		edges= new ArrayList<Edge>();
		//addNode(Color.green,"A");
		
		//test = new Node(Color.green, nextPoint, 20, "A");
	}
	
	//paint method
	@Override
	public void paint(Graphics g){
		//we translate the origin down by 22 pixels to offset the border of the frame
		g.translate(0,22);
		
		//set the background colour
		g.setColor(Color.white);
		//draw the background
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//draw each edge
		for(Edge edge:edges){
			edge.draw(g);
		}
		
		//draw each of the nodes
		for(Node node:nodes){
			node.draw(g);
		}
		/*g.setColor(Color.RED);
		g.fillOval(480, 480, 200, 200);*/
		//nodeOne.draw(g);
		//test.draw(g);
	}
	
	//method to add a node
	public void addNode(Color col, String text){
		//creates a new node with the node color and text, at the point nextPoint
		Node node = new Node(col, nextPoint, 10,text);
		
		//gets the coords
		int xCoord = nextPoint.getXCoord();
		int yCoord = nextPoint.getYCoord();
		
		//moves the next point 50 along, if it goes out the window we reset it to x coord 50 and move it down
		xCoord=xCoord+width/5;
		if(xCoord>width-20){
			xCoord=50;
			yCoord=yCoord+height/6;
		}
		//sets the next point
		nextPoint = new Point(xCoord,yCoord);
		//adds the node to the list of nodes
		nodes.add(node);
		
		//repaints the window
		repaint();
	}
	
	//adds an edge
	public void addEdge(Color col, String textIdOne,String textIdTwo, String label){
		Node nodeOne = null;
		Node nodeTwo = null;
		
		//gets which of the nodes are the end points from the given text ids
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
		
		Edge edge;
		//if the nodes are under each other the edge is an arc, to stop it going through other nodes,
		//unless they are directly below each other at which point it is a straight line
		if(nodeOne.getCoords().getXCoord()==nodeTwo.getCoords().getXCoord()){
			if(Math.abs(nodeOne.getCoords().getYCoord()-nodeTwo.getCoords().getYCoord())<=height/6){
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,false);
			}
			else{
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,true);
			}
		}
		//if the nodes are next to each other the edge is an arc, to stop it going through other nodes,
		//unless they are directly next to each other at which point it is a straight line
		else if(nodeOne.getCoords().getYCoord()==nodeTwo.getCoords().getYCoord()){
			if(Math.abs(nodeOne.getCoords().getXCoord()-nodeTwo.getCoords().getXCoord())<=(width/5)){
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,false);
			}
			else{
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,true);
			}
		}
		//otherwise the edge is a straight line
		else{
			edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,false);
		}
		
		//adds the edge to the list of edges
		edges.add(edge);
		//repaints the graph
		repaint();
	}
	
	public static void main(String[] args){
		
		java.awt.EventQueue.invokeLater(new Runnable(){
				public void run(){
					GraphDrawer graphDrawer = new GraphDrawer();
					
					graphDrawer.addNode(Color.white, "A");
					graphDrawer.addNode(Color.white, "B");
					graphDrawer.addNode(Color.white, "C");
					
					graphDrawer.addNode(Color.white, "D");
					graphDrawer.addNode(Color.white, "E");					
					graphDrawer.addNode(Color.white, "F");
					
					graphDrawer.addNode(Color.white, "G");
					graphDrawer.addNode(Color.white, "H");
					graphDrawer.addNode(Color.white, "I");
					
					graphDrawer.addEdge(Color.lightGray, "B", "A", "20");
					graphDrawer.addEdge(Color.lightGray, "F", "A", "15");
					graphDrawer.addEdge(Color.lightGray, "A", "C", "30");
					
					graphDrawer.addEdge(Color.lightGray, "A", "G", "5");
					graphDrawer.addEdge(Color.lightGray, "B", "F", "10");
					graphDrawer.addEdge(Color.lightGray, "G", "E", "1");
					
					graphDrawer.addEdge(Color.lightGray, "C","H","4");
					graphDrawer.addEdge(Color.lightGray,"A", "I","13");
				}
			}
		);
		
		
		
		
		//c.paint(null);
	}
}