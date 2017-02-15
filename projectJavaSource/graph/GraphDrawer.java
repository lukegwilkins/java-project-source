package graph;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import graph.graphicsObjects.*;

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
		Node node = new Node(col, nextPoint, 10,text);
		
		int xCoord = nextPoint.getXCoord();
		int yCoord = nextPoint.getYCoord();
		
		xCoord=xCoord+width/5;
		if(xCoord>width-20){
			xCoord=50;
			yCoord=yCoord+height/6;
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
		
		Edge edge;
		if(nodeOne.getCoords().getXCoord()==nodeTwo.getCoords().getXCoord()){
			if(Math.abs(nodeOne.getCoords().getYCoord()-nodeTwo.getCoords().getYCoord())<=height/6){
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,false);
			}
			else{
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,true);
			}
		}
		else if(nodeOne.getCoords().getYCoord()==nodeTwo.getCoords().getYCoord()){
			if(Math.abs(nodeOne.getCoords().getXCoord()-nodeTwo.getCoords().getXCoord())<=(width/5)){
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,false);
			}
			else{
				edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,true);
			}
		}
		else{
			edge = new Edge(col, nodeOne.getCoords(), nodeTwo.getCoords(),label,false);
		}
		
		edges.add(edge);
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