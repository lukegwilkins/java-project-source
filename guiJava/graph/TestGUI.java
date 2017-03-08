import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import graphicsObjects.*;

public class TestGUI extends JFrame{
	private Node nodeOne;
	private Edge edgeOne;
	public TestGUI(){
		setTitle("CircleGUI");
		setSize(960,960);
		//setLocation(100,100);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Point coords = new Point(480,480);
		nodeOne= new Node(Color.green, coords, 50,"A");
		
		Point pointOne = new Point(0,0);
		Point pointTwo = new Point(600,600);
		
		edgeOne = new Edge(Color.black, pointOne, pointTwo,"20", false);
	}
	
	@Override
	public void paint(Graphics g){
		g.translate(0,20);
		//getContentPane().setBackground(Color.GREEN);
		//set the background colour
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		/*g.setColor(Color.RED);
		g.fillOval(480, 480, 200, 200);*/
		nodeOne.draw(g);
		
		edgeOne.draw(g);
	}
	
	public static void main(String[] args){
		TestGUI test = new TestGUI();
		
		//c.paint(null);
	}
}