import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

public class BombeGUI extends JFrame
					  implements ActionListener{
	private int height;
	private int width;
	
	private JButton menuButton;
	private JButton crackEnigmaButton;
	
	//private JButton secondButton;
	private JTextField crib;
	private JTextArea cipherText;
	private JTextField cribPosition;
	
	private BombeMachine bombe;
	private GridBagConstraints constraints;
	
	private JTextArea closuresOutput;
	private JTextArea tailsOutput;
	private JTextArea resultsOutput;
	
	private ArrayList<ArrayList<String>> closures;
	private ArrayList<ArrayList<String>> tails;
	
	public BombeGUI(){
		bombe = new BombeMachine(this);
		height=600;
		width=600;
		setTitle("Bombe GUI");
		setSize(width, height);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//set layout to be a grid bag layout
		getContentPane().setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints();
		//want everything to be to the left
		//constraints.anchor = GridBagConstraints.WEST;
		//how much padding to put at top, left, right and bottom (might not be that order in constructored)
		constraints.insets = new Insets(3,3,3,3);
		
		//setup the panes for the GUI
		addInputFields();		
		addButtons();
		addOutputFields();
		
		pack();
	}
	
	public void addButtons(){
		menuButton = new JButton("Generate menu");
		menuButton.setVerticalTextPosition(AbstractButton.CENTER);
		menuButton.setHorizontalTextPosition(AbstractButton.LEADING);
		menuButton.setMnemonic(KeyEvent.VK_D);
		menuButton.setActionCommand("genMenu");
		
		menuButton.addActionListener(this);
		
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 2;
		constraints.gridy = 6;
		getContentPane().add(menuButton, constraints);
		
		crackEnigmaButton = new JButton("Crack Enigma");
		crackEnigmaButton.setVerticalTextPosition(AbstractButton.CENTER);
		crackEnigmaButton.setHorizontalTextPosition(AbstractButton.LEADING);
		crackEnigmaButton.setMnemonic(KeyEvent.VK_D);
		crackEnigmaButton.setActionCommand("crackEnigma");
		
		crackEnigmaButton.addActionListener(this);
		
		//constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 3;
		constraints.gridy = 6;
		getContentPane().add(crackEnigmaButton, constraints);
		
	}
	
	public void addInputFields(){
		crib = new JTextField(20);
		JLabel cribLabel = new JLabel("Crib:");
		
		cipherText = new JTextArea(8,30);
		JLabel cipherTextLabel = new JLabel("Ciphertext:");
		//makes text go onto new lines if it is too long
		cipherText.setLineWrap(true);
		
		//makes it so the textbox scrolls
		JScrollPane cipherTextScrollPane = new JScrollPane(cipherText);
		//use default scroll bar policy (display when needed);
		//cipherTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cipherTextScrollPane.setPreferredSize(new Dimension(250,268));
		
		cribPosition = new JTextField(8);
		JLabel cribPositionLabel = new JLabel("Crib Postion:");
		constraints.anchor = GridBagConstraints.LINE_START;
		
		constraints.gridx=0;
		constraints.gridy=0;
		getContentPane().add(cribLabel, constraints);
		
		constraints.gridx=1;
		constraints.gridy=0;
		getContentPane().add(cribPositionLabel, constraints);
		
		//constraints.fill =GridBagConstraints.HORIZONTAL;
		constraints.gridx=0;
		constraints.gridy=1;
		getContentPane().add(crib, constraints);
		
		constraints.gridx=1;
		constraints.gridy=1;
		getContentPane().add(cribPosition, constraints);
		
		constraints.gridx=0;
		constraints.gridy=2;
		getContentPane().add(cipherTextLabel, constraints);
		
		constraints.gridx=0;
		constraints.gridy=3;
		constraints.gridheight = 3;
		getContentPane().add(cipherTextScrollPane, constraints);
		
		//reset the how much of the columns we span
		constraints.gridheight = 1;
	}
	
	public void addOutputFields(){
		closuresOutput = new JTextArea();
		closuresOutput.setEditable(false);
		closuresOutput.setLineWrap(true);
		
		JScrollPane closuresScrollOutput = new JScrollPane(closuresOutput);
		//.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		closuresScrollOutput.setPreferredSize(new Dimension(340,120));
		
		JLabel closuresLabel = new JLabel("Closures Found:");
		constraints.anchor = GridBagConstraints.LINE_START;
		//want these to span 3 columns
		constraints.gridwidth=3;
		
		constraints.gridx=1;
		constraints.gridy=2;
		getContentPane().add(closuresLabel, constraints);
		
		constraints.gridx=1;
		constraints.gridy=3;
		getContentPane().add(closuresScrollOutput, constraints);
		
		tailsOutput = new JTextArea();
		tailsOutput.setEditable(false);
		tailsOutput.setLineWrap(true);
		
		JScrollPane tailsScrollOutput = new JScrollPane(tailsOutput);
		tailsScrollOutput.setPreferredSize(new Dimension(340,120));
		
		JLabel tailsLabel = new JLabel("Tails Found:");
		constraints.gridx = 1;
		constraints.gridy = 4;
		getContentPane().add(tailsLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 5;
		getContentPane().add(tailsScrollOutput, constraints);
		
		//reset the column span
		constraints.gridwidth=1;
		
		JLabel resultsLabel = new JLabel("Results:");
		constraints.gridx = 4;
		constraints.gridy = 2;
		getContentPane().add(resultsLabel, constraints);
		
		resultsOutput = new JTextArea();
		resultsOutput.setEditable(false);
		resultsOutput.setLineWrap(true);
		//resultsOutput.setText("test");
		
		JScrollPane resultsScrollOutput = new JScrollPane(resultsOutput);
		resultsScrollOutput.setPreferredSize(new Dimension(250, 268));
		constraints.gridheight = 3;
		constraints.gridx = 4;
		constraints.gridy = 3;
		getContentPane().add(resultsScrollOutput, constraints);
		
		//reset row span
		constraints.gridheight = 1;
	}
	
	public void generateMenu(){
		//String cribString = 
		//String cipher = cipherText.getText();
		int position = Integer.parseInt(cribPosition.getText());
		bombe.setCrib(crib.getText());
		bombe.setCipherText(cipherText.getText());
		
		ArrayList<ArrayList<ArrayList<String>>> closuresAndTails = bombe.generateClosuresAndTails(position);
		closures = closuresAndTails.get(0);
		tails = closuresAndTails.get(1);
		
		closuresOutput.setText("");
		for(int i=0;i<closures.size();i++){
			closuresOutput.append(""+(i+1)+". "+closures.get(i)+"\n");
		}
		
		tailsOutput.setText("");
		for(int i=0;i<tails.size();i++){
			tailsOutput.append(""+(i+1)+". "+tails.get(i)+"\n");
		}
		
		
		//System.out.println(closuresToBeUsed);
		//System.out.println(tailsToBeUsed);
		//bombe.crackEnigma(closuresToBeUsed, tailsToBeUsed);
		//this.closures.setText(closures.toString());
	}
	
	public void crackEnigma(){
		int numOfClosures = 3;
		ArrayList<String> closuresToBeUsed = new ArrayList<String>();
		
		for(int i=0;i<numOfClosures && i<closures.size();i++){
			//get the closure and convert it to a string								
			String closure = closures.get(i).get(0);
			
			//converts the closure to a string
			for(int j=1;j<closures.get(i).size()-1;j++){
				closure=closure+","+closures.get(i).get(j);
			}
			
			//adds the closure to the array list
			closuresToBeUsed.add(closure);
		}
		//update the GUI
		//repaint();
		
		//give crackEnigma its own thread
		ArrayList<ArrayList<String>> tailsToBeUsed = bombe.edgePicker(closuresToBeUsed, tails);
		System.out.println("hello");
		Thread resultsThread = new Thread(new Runnable(){
			public void run(){
				bombe.crackEnigma(closuresToBeUsed, tailsToBeUsed);
			}
		});
		
		resultsThread.start();
		//outputResultsln("hello");
	}
	
	public void outputResultsln(String text){
		resultsOutput.append(text+"\n");
	}
	
	public void actionPerformed(ActionEvent e){
		if("genMenu".equals(e.getActionCommand())){
			generateMenu();
		}
		else if("crackEnigma".equals(e.getActionCommand())){
			crackEnigma();
		}
	}
	public static void main(String[] args){
		java.awt.EventQueue.invokeLater(new Runnable(){
				public void run(){
					BombeGUI bombeGUI = new BombeGUI();
				}
			}
		);
	}
}