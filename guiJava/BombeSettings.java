import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.*;

public class BombeSettings extends JFrame
						   implements ActionListener{
	//sets up variables for use in the program
	private int height;
	private int width;
	
	private GridBagConstraints constraints;
	
	private JTextField noOfClosuresInput;
	
	private BombeGUI bombeGUI;
	
	private JLabel noOfClosureLabel;
	
	private ArrayList<JCheckBox> reflectors;
	
	//constructor for the settings window
	public BombeSettings(BombeGUI parentWindow){
		//sets the window width and height
		height=600;
		width=600;
		
		//set the window title
		setTitle("Bombe Settings");
		setSize(width, height);
		//sets the window to be visible
		setVisible(true);
		//makes it so that the window cannot be resized
		setResizable(false);
		//sets it so that just the window closes when the window is closed
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//stores the window that spawned the settings window
		bombeGUI=parentWindow;
		//set layout to be a grid bag layout
		getContentPane().setLayout(new GridBagLayout());
		
		//sets the constraints
		constraints = new GridBagConstraints();
		
		//how much padding to put at top, left, right and bottom
		constraints.insets = new Insets(3,3,3,3);
		
		//sets up the options to change how many closures are used
		setupNoOfClosuresInput();
		setupNoOfClosuresButton();
		setupNoOfClosureLabel();
		
		//sets up the options to set which reflectors can be used
		setupReflectorsInput();
		changeReflectors();
		
		//packs the window so everything is displayed
		pack();
	}
	
	//sets up the textbox to input how many closures are used
	public void setupNoOfClosuresInput(){
		//sets up the constraints
		constraints.gridx=0;
		constraints.gridy=3;
		constraints.gridwidth=2;
		
		//sets up the textbox and adds it to the window
		noOfClosuresInput = new JTextField(15);
		getContentPane().add(noOfClosuresInput, constraints);
		//resets the column span
		constraints.gridwidth=1;
	}
	
	//sets up the button to change how many closures are used
	public void setupNoOfClosuresButton(){
		//creates the button to change the number of closures
		JButton button = new JButton("Change no of closures");
		
		//sets up the command for the button
		button.setVerticalTextPosition(AbstractButton.CENTER);
		button.setHorizontalTextPosition(AbstractButton.LEADING);
		button.setMnemonic(KeyEvent.VK_D);
		button.setActionCommand("changeClosureNo");
		//sets the buttons action listener
		button.addActionListener(this);
		
		//sets the constraints 
		constraints.gridx=2;
		constraints.gridy=3;
		//adds the button to the window
		getContentPane().add(button, constraints);
	}
	
	//sets up the label to say how many closures are currently been used
	public void setupNoOfClosureLabel(){
		//creates the label and the constraints
		noOfClosureLabel = new JLabel();
		constraints.gridx=0;
		constraints.gridy=2;
		
		//adds the label to the window
		getContentPane().add(noOfClosureLabel, constraints);
		
		//updates the label so that correct number of closures been used is displayed
		updateNoOfClosureLabel();
	}
	
	//sets it so that the noOfClosureLabel displays how many closures are in use
	public void updateNoOfClosureLabel(){
		noOfClosureLabel.setText("Number of Closures used is: "+bombeGUI.getNumOfClosures());
	}
	
	//method that is called to change the number of closures used
	public void changeClosureNo(){
		//gets the user input and sets the number of closures used
		bombeGUI.setNumOfClosures(Integer.parseInt(noOfClosuresInput.getText()));
		//updates the number of closures used display
		updateNoOfClosureLabel();
	}
	
	//method for setting up the reflector input
	public void setupReflectorsInput(){
		//sets constraints
		constraints.gridy=0;
		constraints.gridx=1;
		//creates a label for the reflector input
		JLabel reflectorLabel = new JLabel("Reflectors");
		
		//adds the label to window
		getContentPane().add(reflectorLabel, constraints);
		
		constraints.gridy=1;
		//setss up a new araylist to contain all the check boxes
		reflectors = new ArrayList<JCheckBox>();
		
		//creates a checkbox for each reflector
		for(int i=0;i<3;i++){
			//creates a new textbox
			JCheckBox temp = new JCheckBox(""+((char)(97+i)));
			temp.setMnemonic(KeyEvent.VK_C);
			temp.setSelected(true);
			//sets it action
			temp.setActionCommand("reflector");
			//sets the action listener for when a check box is pressed
			temp.addActionListener(this);
			constraints.gridx=i;
			
			//adds the checkbox to the window
			getContentPane().add(temp, constraints);
			//stores the checkbox in reflectors
			reflectors.add(temp);
		}
		//updates it so that the appropriate boxes are checked
		updateReflectorCheckBoxes();
	}
	
	//method to tick the boxes for which reflectors are enabled
	public void updateReflectorCheckBoxes(){
		//get the usable reflectors
		ArrayList<Character> useableReflectors = bombeGUI.getBombe().getUsableReflectors();
		
		for(int i=0; i<3;i++){
			//if the reflector is enabled then its checkbox is ticked
			if(useableReflectors.contains((char)(97+i))){
				reflectors.get(i).setSelected(true);
			}
			//otherwise it isn't
			else{
				reflectors.get(i).setSelected(false);
			}
		}
	}
	
	//changes which reflectors are enabled
	public void changeReflectors(){
		//creates a new arraylist to store which reflectors are ticked
		ArrayList<Character> useableReflectors = new ArrayList<Character>();
		
		for(int i=0;i<3;i++){
			//if the reflector's textbox is enabled then it is added to the arraylist
			if(reflectors.get(i).isSelected()){
				useableReflectors.add((char)(97+i));
			}
		}
		//sets the usable reflectors
		bombeGUI.getBombe().setUseableReflectors(useableReflectors);
		
		//if 1 or less reflectors are enabled then the ticked boxes are no longer enabled
		if(useableReflectors.size() <=1){
			disableSelectedReflectors();
		}
		//otherwise they are enabled
		else{
			enableReflectors();
		}
	}
	
	//method that disables any boxes that are ticked
	public void disableSelectedReflectors(){
		for(int i=0;i<3;i++){
			if(reflectors.get(i).isSelected()){
				reflectors.get(i).setEnabled(false);
			}
		}
	}
	
	//method that enables all the boxes
	public void enableReflectors(){
		for(int i=0;i<3;i++){
			reflectors.get(i).setEnabled(true);
		}
	}
	
	//method to handle any actions 
	public void actionPerformed(ActionEvent e){
		//if the command is "changeClosureNo" then the changeClosureNo method is run
		if(e.getActionCommand().equals("changeClosureNo")){
			changeClosureNo();
		}
		//if the command is "reflector" then the changeReflectors method is run
		else if(e.getActionCommand().equals("reflector")){
			changeReflectors();
		}
	}
}