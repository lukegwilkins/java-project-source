package graph;

import java.util.*;
import java.awt.EventQueue;
import java.awt.Color;

public class Graph{
	
	private GraphDrawer graphDrawer;
	
	public Graph(HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu){
		ArrayList<Character> nodes = new ArrayList<Character>();
		ArrayList<String> edges = new ArrayList<String>();
		
		for(Character charOne: menu.keySet()){
			nodes.add(charOne);
			for(Character charTwo: menu.get(charOne).keySet()){
				if(!(nodes.contains(charTwo))){
					edges.add(charOne+" "+charTwo);
				}
			}
		}
		
		System.out.println(nodes);
		System.out.println(edges);
		
		java.awt.EventQueue.invokeLater(new Runnable(){
				public void run(){
					graphDrawer = new GraphDrawer();
					
					
				}
			}
		);
		
		//wait for graphDrawer to set itself up
		try {
			Thread.sleep(100);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		
		for(Character node:nodes){
			graphDrawer.addNode(Color.white, ("" + node).toUpperCase());
		}
		
		for(String edge: edges){
			String[] points = edge.split(" ");
			ArrayList<Integer> positions = menu.get(points[0].charAt(0)).get(points[1].charAt(0));
			
			String label="";
			for(Integer position:positions){
				label=label+position+",";
			}
			
			label=label.substring(0, label.length()-1);
			graphDrawer.addEdge(Color.lightGray, points[0].toUpperCase(), points[1].toUpperCase(), label);
		}
	}
	
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