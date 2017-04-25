package graph;

import java.util.*;
import java.awt.EventQueue;
import java.awt.Color;

public class Graph{
	
	//declares a GraphDrawer for use in the program
	private GraphDrawer graphDrawer;
	
	public Graph(HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu){
		//sets up arraylists for nodes and edges
		ArrayList<Character> nodes = new ArrayList<Character>();
		ArrayList<String> edges = new ArrayList<String>();
		
		//gets what the nodes will be in the graph
		for(Character charOne: menu.keySet()){
			nodes.add(charOne);
			for(Character charTwo: menu.get(charOne).keySet()){
				if(!(nodes.contains(charTwo))){
					edges.add(charOne+" "+charTwo);
				}
			}
		}
		
		//creates a new graphDrawer
		graphDrawer = new GraphDrawer();
		
		//wait for graphDrawer to set itself up
		try {
			Thread.sleep(500);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		
		//adds each node to the graphDrawer with a white colour
		for(Character node:nodes){
			graphDrawer.addNode(Color.white, ("" + node).toUpperCase());
		}
		
		//gets the edges in the graph and adds them
		for(String edge: edges){
			//splits the edge up to get the end points
			String[] points = edge.split(" ");
			//gets the edge's labels
			ArrayList<Integer> positions = menu.get(points[0].charAt(0)).get(points[1].charAt(0));
			
			//merge the labels into one string
			String label="";
			for(Integer position:positions){
				label=label+position+",";
			}
			
			//remove last ","
			label=label.substring(0, label.length()-1);
			//add the edge to the graph
			graphDrawer.addEdge(Color.lightGray, points[0].toUpperCase(), points[1].toUpperCase(), label);
		}
	}
	
	//gets the GraphDrawer for the window
	public GraphDrawer getGraphDrawer(){
		return graphDrawer;
	}
	
	public static void main(String[] args){
		HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu = new HashMap<Character,HashMap<Character,ArrayList<Integer>>>();
		HashMap<Character,ArrayList<Integer>> neighboursOfA = new HashMap<Character,ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(1);
		neighboursOfA.put('b',temp);
		temp = new ArrayList<Integer>();
		temp.add(2);
		neighboursOfA.put('c',temp);
		temp = new ArrayList<Integer>();
		temp.add(7);
		neighboursOfA.put('g',temp);
		menu.put('a',neighboursOfA);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfB = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(1);
		neighboursOfB.put('a',temp);
		temp = new ArrayList<Integer>();
		temp.add(3);
		neighboursOfB.put('d',temp);
		menu.put('b',neighboursOfB);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfC = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(2);
		neighboursOfC.put('a',temp);
		temp = new ArrayList<Integer>();
		temp.add(4);
		neighboursOfC.put('d',temp);
		temp = new ArrayList<Integer>();
		temp.add(6);
		neighboursOfC.put('f',temp);
		menu.put('c',neighboursOfC);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfD = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(3);
		neighboursOfD.put('b',temp);
		temp = new ArrayList<Integer>();
		temp.add(4);
		neighboursOfD.put('c',temp);
		temp = new ArrayList<Integer>();
		temp.add(5);
		temp.add(10);
		neighboursOfD.put('e',temp);
		menu.put('d',neighboursOfD);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfE = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(5);
		temp.add(10);
		neighboursOfE.put('d',temp);
		temp = new ArrayList<Integer>();
		temp.add(8);
		neighboursOfE.put('f',temp);
		menu.put('e',neighboursOfE);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfF = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(6);
		neighboursOfF.put('c',temp);
		temp = new ArrayList<Integer>();
		temp.add(8);
		neighboursOfF.put('e',temp);
		temp = new ArrayList<Integer>();
		temp.add(9);
		neighboursOfF.put('h',temp);
		menu.put('f',neighboursOfF);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfG = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(7);
		neighboursOfG.put('a',temp);
		menu.put('g',neighboursOfG);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfH = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(9);
		neighboursOfH.put('f',temp);
		menu.put('h', neighboursOfH);
		
		Graph graph = new Graph(menu);
	}
}