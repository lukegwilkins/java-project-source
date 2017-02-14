import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import graphicsObjects.*;

public class CircleGUI extends JFrame{
	private Circle circleOne;
	
	public CircleGUI(){
		setTitle("CircleGUI");
		setSize(960,960);
		//setLocation(100,100);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		circleOne= new Circle(Color.green, 480, 480, 50,"A");
	}
	
	public void paint(Graphics g){
		//getContentPane().setBackground(Color.GREEN);
		//background
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		/*g.setColor(Color.RED);
		g.fillOval(480, 480, 200, 200);*/
		circleOne.draw(g);
		
	}
	
	public static void main(String[] args){
		CircleGUI c = new CircleGUI();
		//c.paint(null);
	}
}