import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.*;

public class BombeGUI extends JFrame
					  implements ActionListener{
	//sets up variables for use in the program
	private int height;
	private int width;
	
	private JButton menuButton;
	private JButton crackEnigmaButton;
	private JButton settingsButton;
	
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
	
	private JLabel currentPermutation;
	
	private int cribPositionInt;
	
	private int numOfClosures;
	
	//BombeGUI constructor
	public BombeGUI(){
		//creates a new BombeMachine for use in the program
		bombe = new BombeMachine(this);
		height=600;
		width=600;
		
		//sets the window title
		setTitle("Bombe GUI");
		//sets the window size
		setSize(width, height);
		//makes the window visible
		setVisible(true);
		//sets it so the window isn't resizeable 
		setResizable(false);
		//sets it so the entire program ends when this window closes
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//set layout to be a grid bag layout
		getContentPane().setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints();
		//how much padding to put at top, left, right and bottom (might not be that order in constructored)
		constraints.insets = new Insets(3,3,3,3);
		
		//setup the panes for the GUI
		addInputFields();		
		addButtons();
		addOutputFields();
		
		//default number of closures that are used
		numOfClosures=3;
		//pack all the panes to the window
		pack();
	}
	
	//method to add the buttons to the window
	private void addButtons(){
		//sets up the button to open the settings menu
		settingsButton = new JButton("settings");
		settingsButton.setVerticalTextPosition(AbstractButton.CENTER);
		settingsButton.setHorizontalTextPosition(AbstractButton.LEADING);
		settingsButton.setMnemonic(KeyEvent.VK_D);
		//sets the button command
		settingsButton.setActionCommand("openSettings");
		
		//sets the action listener
		settingsButton.addActionListener(this);
		//sets up the constraints
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		
		constraints.gridy = 6;
		//adds the buttons to the window
		getContentPane().add(settingsButton, constraints);
		
		//creates the button to generate the menu
		menuButton = new JButton("Generate menu");
		menuButton.setVerticalTextPosition(AbstractButton.CENTER);
		menuButton.setHorizontalTextPosition(AbstractButton.LEADING);
		menuButton.setMnemonic(KeyEvent.VK_D);
		//set the action command for the button
		menuButton.setActionCommand("genMenu");
		//set the action listener for the button
		menuButton.addActionListener(this);
		
		//set up the constraints
		constraints.gridx = 2;
		
		constraints.gridy = 6;
		//add the button to the window
		getContentPane().add(menuButton, constraints);
		
		//creates the button to crack the enigma code
		crackEnigmaButton = new JButton("Crack Enigma");
		crackEnigmaButton.setVerticalTextPosition(AbstractButton.CENTER);
		crackEnigmaButton.setHorizontalTextPosition(AbstractButton.LEADING);
		crackEnigmaButton.setMnemonic(KeyEvent.VK_D);
		//sets the button command
		crackEnigmaButton.setActionCommand("crackEnigma");
		
		//sets the action listener for the button
		crackEnigmaButton.addActionListener(this);
		
		//sets up the constraints for the button
		constraints.gridx = 3;
		constraints.gridy = 6;
		//adds the button to the window
		getContentPane().add(crackEnigmaButton, constraints);
		
	}
	
	//method to add the input fields
	private void addInputFields(){
		//creates the textbox and label for the crib
		crib = new JTextField(20);
		JLabel cribLabel = new JLabel("Crib:");
		
		//creates the text area and label for the ciphertext
		cipherText = new JTextArea(8,30);
		JLabel cipherTextLabel = new JLabel("Ciphertext:");
		//makes text go onto new lines if it is too long
		cipherText.setLineWrap(true);
		
		//makes it so the textbox scrolls
		JScrollPane cipherTextScrollPane = new JScrollPane(cipherText);
		//use default scroll bar policy (display when needed);
		//cipherTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//sets the size of the textbox 
		cipherTextScrollPane.setPreferredSize(new Dimension(250,268));
		
		//sets up the textbox to input where the crib is
		cribPosition = new JTextField(8);
		//sets up the label for the crib position
		JLabel cribPositionLabel = new JLabel("Crib Postion:");
		//sets up the crib label constraints
		constraints.anchor = GridBagConstraints.LINE_START;
		
		constraints.gridx=0;
		constraints.gridy=0;
		//adds the crib label to the window
		getContentPane().add(cribLabel, constraints);
		
		//sets up constraints for the crib position label
		constraints.gridx=1;
		constraints.gridy=0;
		//adds the crib position label to the window
		getContentPane().add(cribPositionLabel, constraints);
		
		//sets up constraints for the crib input
		constraints.gridx=0;
		constraints.gridy=1;
		//adds the text box to the window
		getContentPane().add(crib, constraints);
		
		//sets the constraints for the crib position input
		constraints.gridx=1;
		constraints.gridy=1;
		//adds the text box to the window
		getContentPane().add(cribPosition, constraints);
		
		//sets the constraints for the cipher text label
		constraints.gridx=0;
		constraints.gridy=2;
		//adds it to the window
		getContentPane().add(cipherTextLabel, constraints);
		
		//sets the constraints for the ciphertext input
		constraints.gridx=0;
		constraints.gridy=3;
		constraints.gridheight = 3;
		//adds the cipher text input to the window
		getContentPane().add(cipherTextScrollPane, constraints);
		
		//reset the how manys columns we span
		constraints.gridheight = 1;
	}
	
	//sets up the output fields
	private void addOutputFields(){
		//sets up the closure output field
		closuresOutput = new JTextArea();
		//sets it so the user can't write in it
		closuresOutput.setEditable(false);
		closuresOutput.setLineWrap(true);
		
		//makes it scrollable
		JScrollPane closuresScrollOutput = new JScrollPane(closuresOutput);
		//.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//sets the size
		closuresScrollOutput.setPreferredSize(new Dimension(340,120));
		
		//creates a label for the closure output box
		JLabel closuresLabel = new JLabel("Closures Found:");
		//sets up the constraints
		constraints.anchor = GridBagConstraints.LINE_START;
		//want these to span 3 columns
		constraints.gridwidth=3;
		
		constraints.gridx=1;
		constraints.gridy=2;
		//adds the label to the window
		getContentPane().add(closuresLabel, constraints);
		
		constraints.gridx=1;
		constraints.gridy=3;
		//adds the closure output to the window
		getContentPane().add(closuresScrollOutput, constraints);
		
		//creates a textbox to output the tails to
		tailsOutput = new JTextArea();
		tailsOutput.setEditable(false);
		tailsOutput.setLineWrap(true);
		
		//makes the tails output scrollable
		JScrollPane tailsScrollOutput = new JScrollPane(tailsOutput);
		//sets the size
		tailsScrollOutput.setPreferredSize(new Dimension(340,120));
		
		//creates a label for the tails output
		JLabel tailsLabel = new JLabel("Tails Found:");
		//adds the constraints for the tails label
		constraints.gridx = 1;
		constraints.gridy = 4;
		//adds the label to the window
		getContentPane().add(tailsLabel, constraints);
		
		//sets constraints for tails output
		constraints.gridx = 1;
		constraints.gridy = 5;
		//adds the tail output box to the window
		getContentPane().add(tailsScrollOutput, constraints);
		
		//reset the column span
		constraints.gridwidth=1;
		
		//creates the result label
		JLabel resultsLabel = new JLabel("Results:");
		constraints.gridx = 4;
		constraints.gridy = 2;
		//adds the results label to the window
		getContentPane().add(resultsLabel, constraints);
		
		//creates the text area where results are outputted
		resultsOutput = new JTextArea();
		resultsOutput.setEditable(false);
		resultsOutput.setLineWrap(true);
		//resultsOutput.setText("test");
		
		//makes the results area scrollable
		JScrollPane resultsScrollOutput = new JScrollPane(resultsOutput);
		//sets the size of the results area
		resultsScrollOutput.setPreferredSize(new Dimension(250, 268));
		
		//sets the constraints for the results output
		constraints.gridheight = 3;
		constraints.gridx = 4;
		constraints.gridy = 3;
		//adds the results area to the window
		getContentPane().add(resultsScrollOutput, constraints);
		
		//reset row span
		constraints.gridheight = 1;
		
		//sets the constraints for the current permutation label
		constraints.gridx = 4;
		constraints.gridy = 0;
		
		//adds the label to the window
		JLabel permutationLabel = new JLabel("Current permutation:");
		getContentPane().add(permutationLabel, constraints);
		
		//creates a label for the current permutation which is been tested by the BOMBE
		currentPermutation = new JLabel("test");
		constraints.gridy=1;
		
		//adds it to the window
		getContentPane().add(currentPermutation, constraints);
	}
	
	//method to generate the menu
	public void generateMenu(){
		//gets the crib postion
		int position = Integer.parseInt(cribPosition.getText());
		
		cribPositionInt = position;
		
		//sets the crib and ciphertext in the Bombe
		bombe.setCrib(crib.getText());
		bombe.setCipherText(cipherText.getText());
		
		//gets the closures and tails from the Bombe
		ArrayList<ArrayList<ArrayList<String>>> closuresAndTails = bombe.generateClosuresAndTails(position);
		//stores the closures and tails
		closures = closuresAndTails.get(0);
		tails = closuresAndTails.get(1);
		
		//resets the closures output text
		closuresOutput.setText("");
		//outputs each closure
		for(int i=0;i<closures.size();i++){
			closuresOutput.append(""+(i+1)+". "+closures.get(i)+"\n");
		}
		
		//outputs the tails
		tailsOutput.setText("");
		for(int i=0;i<tails.size();i++){
			tailsOutput.append(""+(i+1)+". "+tails.get(i)+"\n");
		}
	}
	
	//method for cracking the Enigma
	public void crackEnigma(){
		//gets the closures to be used
		ArrayList<ArrayList<String>> closuresToBeUsed = bombe.closureSelector(closures,numOfClosures);
		//Collections.sort(closures, new ClosureCompactCompare());
		//converts the closures to strings
		ArrayList<String> closureStrings = new ArrayList<String>();
		for(int i=0;i<numOfClosures && i<closuresToBeUsed.size();i++){
			//get the closure and convert it to a string								
			String closure = closuresToBeUsed.get(i).get(0);
			
			//converts the closure to a string
			for(int j=1;j<closuresToBeUsed.get(i).size()-1;j++){
				closure=closure+","+closuresToBeUsed.get(i).get(j);
			}
			
			//adds the closure to the array list
			closureStrings.add(closure);
		}
		
		//gets the closures to be used
		ArrayList<ArrayList<String>> tailsToBeUsed = bombe.edgePicker(closureStrings, tails);
		
		//reset the results output
		resultsOutput.setText("");
		
		//gets the Bombe to crack the Enigma in its own thread
		Thread resultsThread = new Thread(new Runnable(){
			public void run(){
				bombe.crackEnigma(closureStrings, tailsToBeUsed, cribPositionInt);
			}
		});
		
		resultsThread.start();
		//outputResultsln("hello");
	}
	
	//method to add a result to the results output
	public void outputResultsln(String text){
		resultsOutput.append(text+"\n");
	}
	
	//method to change the permuation output
	public void changePermutation(String permutation){
		currentPermutation.setText(permutation);
	}
	
	//method to set how many closures are used
	public void setNumOfClosures(int numOfClosures){
		this.numOfClosures=numOfClosures;
	}
	
	//method to get how many closures are used
	public int getNumOfClosures(){
		return numOfClosures;
	}
	
	//method to open the settings menu
	public void openSettings(){
		BombeSettings settings = new BombeSettings(this);
	}
	
	//method to get the BombeGUI's bombe
	public BombeMachine getBombe(){
		return bombe;
	}
	
	//method to handle actions
	public void actionPerformed(ActionEvent e){
		//if the "genMenu" action is commanded then the generateMenu method is called.
		if("genMenu".equals(e.getActionCommand())){
			generateMenu();
		}
		//if the "crackEnigma" action is commanded then the crackEnigma() method is called
		else if("crackEnigma".equals(e.getActionCommand())){
			crackEnigma();
		}
		//if the "openSettings" action is commanded then the open settings method is called
		else if("openSettings".equals(e.getActionCommand())){
			openSettings();
		}
	}
	
	//main method for the BombeGUI
	public static void main(String[] args){
		BombeGUI bombeGUI = new BombeGUI();
	
	}
}