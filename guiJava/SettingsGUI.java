import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import enigmaComponents.*;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsGUI extends JFrame implements ActionListener{
	//sets up variables for use in SettingsGUI
	private int height;
	private int width;
	
	private int buttonPadding;
	
	private EnigmaGUI parentWindow;
	
	private ArrayList<JLabel> ringPositionLabels;
	
	private GridBagConstraints constraints;
	
	private JComboBox<String> reflectorMenu;
	private ArrayList<JComboBox<String>> rotorMenus;
	
	private JTextField plugboardInput;
	
	public SettingsGUI(EnigmaGUI parentWindow){
		//sets up height, width and how much padding buttons get
		height = 600;
		width = 600;
		buttonPadding = 40;
		
		//sets window title
		setTitle("Settings Menu");
		//sets size
		setSize(width, height);
		//sets window to be visible
		setVisible(true);
		//on close a separate function is called to re enable the EnigmaGUI window 
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		//stores the EnigmaGUI window that spawned it
		this.parentWindow = parentWindow;
		
		//sets up the close operation
		setUpCloseOperation();
		
		//sets the layout of the window to be a grid
		getContentPane().setLayout(new GridBagLayout());
		
		//sets the constraints for the window
		constraints = new GridBagConstraints();
		
		//sets up a new arraylist to store the ring position labels
		ringPositionLabels = new ArrayList<JLabel>();
		
		//sets up a new arraylist to store the dropdown menus for the rotor permutation
		rotorMenus = new ArrayList<JComboBox<String>>();
		
		//sets up the ring positions
		setUpRingPositions();
		
		//sets up the permutation dropdown menu
		setUpPermutationOptions();
		
		//sets up the plugboard options
		setUpPlugboardOptions();
		
		//packs the window so everything is displayed
		pack();
	}
	
	//sets up the ring labels
	private void setUpRingLabels(){
		//sets up the constraints
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridy=2;
		
		//creates a label to say what the ring labels are and adds it to the window
		JLabel ringPositionsLabel = new JLabel("Ring Positions: ");
		getContentPane().add(ringPositionsLabel, constraints);
		
		//sets up constraints
		constraints.insets = new Insets(3,3,3,buttonPadding);
		constraints.anchor=GridBagConstraints.CENTER;
		
		//adds a label to the window for each rotor's ring position
		for(int i=0; i<3; i++){
			constraints.gridx=i+1;
			
			JLabel temp = new JLabel(""+i);
			ringPositionLabels.add(temp);
			
			getContentPane().add(temp, constraints);
		}		
			
	}
	
	//method for setting up the ring position option
	private void setUpRingPositions(){
		setUpRingLabels();
		setUpRingIncreaseButtons();
		setUpRingDecreaseButtons();
		updateRingPositions();
	}
	
	//method for setting up the buttons to increase the ring position
	private void setUpRingIncreaseButtons(){
		//sets up the constraints 
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=1;
		
		//adds an increase button for each of the rotor's ring positions
		for(int i=0; i<3;i++){
			JButton temp = new JButton("^");
			//sets the buttons command and action listener
			temp.setActionCommand("increaseRing"+i);
			temp.addActionListener(this);
			
			constraints.gridx=i+1;			
			getContentPane().add(temp, constraints);
		}
	}
	
	//method setting up the buttons to decrease the ring positions
	private void setUpRingDecreaseButtons(){
		//sets up the constraints
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=3;
		
		//adds a decrease button for each of the rotor's ring positions
		for(int i=0; i<3;i++){
			JButton temp = new JButton("v");
			//set the buttons command and action listener
			temp.setActionCommand("decreaseRing"+i);
			temp.addActionListener(this);
			
			constraints.gridx=i+1;
			
			getContentPane().add(temp, constraints);
		}
	}
	
	//method used to update the display so the correct ring positions are displayed
	private void updateRingPositions(){
		//gets the right (first) rotor
		Scrambler scrambler=parentWindow.getEnigma().getScrambler();
		Rotor currentRotor = (Rotor) scrambler.getFirstRotor();
		
		for(int i=2;i>=0;i--){
			//updates the rotor's label to display the ring position
			ringPositionLabels.get(i).setText(""+currentRotor.getRingPosition());
			//get the next rotor
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor = (Rotor) currentRotor.getNextRotor();
			}
		}
		
		//tells EnigmaGUI to update its ring positions
		parentWindow.updateRingPos();
	}
	
	//method for increasing a rotor's ring position
	public void increaseRingPos(String actionCommand){
		//gets the first rotor and which rotor had its ring position increased
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		Rotor rotor = (Rotor) parentWindow.getEnigma().getScrambler().getFirstRotor();
		
		//gets the rotor that had its ring position increased
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		//increases the ring position
		int newRingPos = (rotor.getRingPosition()+1)%26;
		
		//the ring position needs to be between 1 and 26 so if we go above 26 we wrap around to 1
		if(newRingPos==0){
			newRingPos=1;
		}
		
		//sets the new ring position
		rotor.setRingPosition(newRingPos);
		//updates the display
		updateRingPositions();
	}
	
	//method to decrease the ring positions
	public void decreaseRingPos(String actionCommand){
		//gets the first rotor and which rotor had its ring position decreased
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		Rotor rotor = (Rotor) parentWindow.getEnigma().getScrambler().getFirstRotor();
		
		//gets the rotor that had its ring position increased
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		//decreases the ring position
		int newRingPos = (25+rotor.getRingPosition())%26;
		
		//the ring position needs to be between 1 and 26 so if we go below 1 we wrap around to 26
		if(newRingPos==0){
			newRingPos=26;
		}
		
		//sets the new ring position
		rotor.setRingPosition(newRingPos);
		//updates the display
		updateRingPositions();		
	}
	
	//method for setting up the permutation options 
	private void setUpPermutationOptions(){
		setUpPermutationLabel();
		
		setUpReflectorMenu();
		setUpPermutationButton();
		
		setUpRotorMenus();
		
		setUpPermMenuSelect();
	}
	
	//method for setting up the label saying "permutation"
	private void setUpPermutationLabel(){
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridy=0;
		constraints.gridx=0;
		
		JLabel temp = new JLabel("Permutation: ");
		
		getContentPane().add(temp,constraints);
	}
	
	//sets up the reflector drop down menu
	private void setUpReflectorMenu(){
		//creates the options for the drop down menu
		String[] reflectors ={"A","B","C"};
		//creates the drop down menu
		reflectorMenu = new JComboBox<String>(reflectors);
		
		reflectorMenu.setSelectedIndex(0);
		
		//sets constraints and adds the drop down menu to the window
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=0;
		constraints.gridx=1;
		
		getContentPane().add(reflectorMenu,constraints);
	}
	
	//sets up rotor number drop down menus
	private void setUpRotorMenus(){
		//sets up menu options
		String[] rotors ={"1","2","3","4","5"};
		//constraints
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=0;
		
		//creates 3 drop down menus, one for right rotor, middle and left
		for(int i=0; i<3; i++){
			JComboBox<String> temp = new JComboBox<String>(rotors);
			constraints.gridx=i+2;
			
			rotorMenus.add(temp);
			getContentPane().add(temp, constraints);
		}
		
	}
	
	//sets it so that the drop down menu's display the current permutation
	private void setUpPermMenuSelect(){
		//gets the current permutation
		String permutation = parentWindow.getEnigma().getPermutation().replaceAll("\\s","");
		//splits the permutation up
		String[] settings = permutation.split(",");
		
		//gets which reflector is in use
		int reflectorIndex = ((int)settings[0].toLowerCase().charAt(0))-97;
		//sets it so the reflector drop down menu is displaying the right reflector
		reflectorMenu.setSelectedIndex(reflectorIndex);
		
		//set it so the rotor drop down menus display the correct rotor
		for(int i=1; i<4; i++){
			int index = Integer.parseInt(settings[i])-1;
			rotorMenus.get(i-1).setSelectedIndex(index);
		}
	}
	
	//sets up the button to change the permutation
	private void setUpPermutationButton(){
		//creates a new button
		JButton permutationButton = new JButton("change permutation");
		//sets the button's command and action listener
		permutationButton.setActionCommand("changePerm");
		permutationButton.addActionListener(this);
		
		//sets the constraints
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=0;
		constraints.gridx=5;
		
		//adds the button to the window
		getContentPane().add(permutationButton, constraints);
		
	}
	
	//method for changing the permutation
	private void changePermutation(){
		//gets which reflector is in use
		String permutation = (String)reflectorMenu.getSelectedItem();
		
		//gets the rest of the permutation
		for(int i=0; i<3; i++){
			permutation = permutation +"," +(String)rotorMenus.get(i).getSelectedItem();
		}
		//sets the permutation
		parentWindow.getEnigma().setPermutation(permutation);
		
		//gets EnigmaGUI to update its permutation display
		parentWindow.updatePermutation();
	}
	
	//sets up what happens when this window is closed
	private void setUpCloseOperation(){
		//creates new WindowListener to control how the window is closed
		WindowListener exitListener = new WindowAdapter(){
			
			@Override
			public void windowClosing(WindowEvent e){
				//we re-enable the EnigmaGUI that made the settings window
				parentWindow.setEnabled(true);
				//make this window invisible
				setVisible(false);
				//and dispose of it
				dispose();
			}
		};
		//adds the WindowListener to the Window
		addWindowListener(exitListener);
	}
	
	//method for setting up the plugboard options
	private void setUpPlugboardOptions(){
		setUpPlugboardInput();
		setUpPlugboardButton();
	}
	
	//sets up the plugboard input
	private void setUpPlugboardInput(){
		//creates a label so the user knows what the textbox is for
		JLabel plugboardLabel = new JLabel("plugboard settings:");
		//sets up the constraints
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridy=4;
		constraints.gridx=0;
		
		//adds the label to the window
		getContentPane().add(plugboardLabel, constraints);
		
		//creates a new textbox 
		plugboardInput = new JTextField(25);
		
		//sets up the constraints
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=4;
		constraints.gridx=1;
		//textbox spans multiple columns
		constraints.gridwidth=4;
		
		//adds the textbox to the window
		getContentPane().add(plugboardInput, constraints);
		//resets the column span
		constraints.gridwidth=1;
	}
	
	//sets up the button to change the plugboard settings
	private void setUpPlugboardButton(){
		//creates a new button
		JButton plugboardButton = new JButton("change settings");
		//sets the buttons command and action listener
		plugboardButton.setActionCommand("changePlug");
		plugboardButton.addActionListener(this);
		
		//sets up the constraints
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=4;
		constraints.gridx=5;
		constraints.gridwidth=1;
		
		//adds the button to the window
		getContentPane().add(plugboardButton, constraints);
		
	}
	
	//method for changing the plugboard settings
	private void changePlugboardSettings(){
		//splits the settings via "," and makes it into an arraylist
		String[] swaps = plugboardInput.getText().split(",");
		ArrayList<String> mapping = new ArrayList<String>(Arrays.asList(swaps));
		
		//sets the plugboard settings
		parentWindow.getEnigma().getPlugboard().setSwapMapping(mapping);
		
		//tells EnigmaGUI to updates its plugboard settings
		parentWindow.updatePlugboard();
	}
	
	//method for controlling what happens when an action occurs
	public void actionPerformed(ActionEvent e){
		//if increase ring position button is pressed then the increaseRingPos method is called
		if(e.getActionCommand().contains("increaseRing")){
			increaseRingPos(e.getActionCommand());
		}
		//if deccrease ring position button is pressed then the decreaseRingPos method is called
		else if(e.getActionCommand().contains("decreaseRing")){
			decreaseRingPos(e.getActionCommand());
		}
		//if the change rotor permutation button is pressed then the changePermutation method is called
		else if(e.getActionCommand().equals("changePerm")){
			changePermutation();
		}
		//if the change plugboard button is pressed then the changePlugboardSettings method is called
		else if(e.getActionCommand().equals("changePlug")){
			changePlugboardSettings();
		}
	}
}