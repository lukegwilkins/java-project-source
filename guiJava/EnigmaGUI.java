import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import enigmaComponents.*;

import java.util.ArrayList;

public class EnigmaGUI extends JFrame implements ActionListener{
	//creates variables which will display the current settings for the enigma machine
	private ArrayList<JLabel> rotorPositionLabels;
	private ArrayList<JLabel> rotorNumberLabels;
	private ArrayList<JLabel> ringPositionLabels;	
	private JLabel plugboardSettings;
	
	//creates variable for the settings button
	private JButton settingsButton;
	
	//creates input and output variables
	private JTextField plainText;
	private JTextArea cipherText;
	
	//window width and height
	private int height;
	private int width;
	
	//padding on buttons
	private int buttonPadding=100;
	//constraints for layouts
	private GridBagConstraints constraints;
	
	//sets up variable to store the enigma machine
	private EnigmaMachine enigma;
	
	//sets up variable to store which reflector is in use
	private JLabel reflectorLabel;
	
	//constructor for the EnigmaGUI
	public EnigmaGUI(){
		//creates an EnigmaMachine for use in the program
		enigma = new EnigmaMachine();
		
		//sets up heigh and width
		height=600;
		width=600;
		
		//sets window title
		setTitle("Enigma GUI");
		setSize(width, height);
		
		//set window to be visible
		setVisible(true);
		//set resizable to be false
		setResizable(false);
		//sets it so that the entire program closes if this window is closed
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//set layout to be a grid bag layout
		getContentPane().setLayout(new GridBagLayout());
		
		//creates new constraints for use to use
		constraints = new GridBagConstraints();
		
		//sets up settings labels
		rotorPositionLabels = new ArrayList<JLabel>();
		rotorNumberLabels = new ArrayList<JLabel>();
		ringPositionLabels = new ArrayList<JLabel>();
		
		settingsLabels();
		
		//outputs the current settings to the labels
		addRotorNumberLabels();
		addRotorPositionLabels();
		addRingPositionLabels();
		
		//sets up buttons for increase and decreasing the rotor positions
		addIncreaseRotorPosButtons();
		addDecreaseRotorPosButtons();
		
		//sets up the settings button
		addSettingsButton();
		
		//sets up input and output
		addMessageInput();
		addCipherTextOutput();
		
		//sets up label to display plugboard settings
		addPlugBoardSettings();
		
		//packs everything so it is displayed in the window
		pack();
	}
	
	//method to add rotor number labels for the rotor permutation
	private void addRotorNumberLabels(){
		//sets up padding arround each label
		constraints.insets = new Insets(3,3,3, buttonPadding);
		//anchors each label to the center of the grid cell
		constraints.anchor = GridBagConstraints.CENTER;
		
		//sets up the grid coordinates
		constraints.gridx=0;
		constraints.gridy=1;
		
		//sets up label for the reflector
		JLabel reflectorOutputLabel = new JLabel("Reflector:");
		//adds label to the window
		getContentPane().add(reflectorOutputLabel, constraints);
		
		//sets up coords
		constraints.gridx=2;
		//creates a new label for the reflector
		reflectorLabel = new JLabel("Test");
		
		//adds reflector label to the window
		getContentPane().add(reflectorLabel,constraints);
		
		//sets up coords
		constraints.gridy=2;
		//adds each label for which rotors are in use
		for(int i=0; i<3; i++){
			//sets up coords
			constraints.gridx=i+1;
			
			//creates new label
			JLabel temp = new JLabel(""+(i+5));
			//stores the label in rotorNumberLabel
			rotorNumberLabels.add(temp);
			
			//on the last label we remove the extra padding for the buttons
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			//adds the label to the window
			getContentPane().add(temp, constraints);
		}
		
		//updates the labels so that the correct permutation is displayed
		updatePermutation();
	}
	
	//method to set up the rotor position labels
	private void addRotorPositionLabels(){
		//sets up padding
		constraints.insets = new Insets(3,3,3, buttonPadding);
		//anchors everything to the centre
		constraints.anchor = GridBagConstraints.CENTER;
		//sets up coords
		constraints.gridy=4;
		
		//creates and adds a label to display each rotor's position
		for(int i=0; i<3; i++){
			//sets up the coords
			constraints.gridx=i+1;
			
			//creates a new label to display the rotor's position
			JLabel temp = new JLabel(""+i);
			//stores the label in rotorPositionLabels
			rotorPositionLabels.add(temp);
			
			//the last label removes the extra padding for the buttons
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			//adds the label to window
			getContentPane().add(temp, constraints);
		}		
		
		//updates it so that the correct rotor positions are displayed
		updateRotorPos();
	}
	
	//adds the labels for the ring positions
	private void addRingPositionLabels(){
		//sets up the padding
		constraints.insets = new Insets(3,3,3, buttonPadding);
		//sets it so everything is centred
		constraints.anchor = GridBagConstraints.CENTER;
		
		//sets up coords
		constraints.gridy=6;
		//adds a label for each ring position
		for(int i=0; i<3; i++){
			//sets up coords
			constraints.gridx=i+1;
			
			//creates a new label to display the ring position
			JLabel temp = new JLabel(""+(i+7));
			//stores the lable in ringPositionLabels
			ringPositionLabels.add(temp);
			
			//we remove the extra padding for the last label
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			//adds the label to the window
			getContentPane().add(temp, constraints);
		}
		
		//updates the labels so they have the correct output
		updateRingPos();
	}
	
	//sets up the labels which say labels are what
	private void settingsLabels(){
		//sets up padding
		constraints.insets = new Insets(3,3,3,3);
		//we want everything anchored to the left
		constraints.anchor = GridBagConstraints.LINE_START;
		
		//sets up coords
		constraints.gridy=2;
		constraints.gridx=0;
		
		//adds rotor number labels
		JLabel rotorNumberLabel = new JLabel("Rotor Numbers: ");
		getContentPane().add(rotorNumberLabel, constraints);
		
		//sets up coords
		constraints.gridy=4;
		
		//adds rotor position labels
		JLabel rotorPositionLabel = new JLabel("Rotor Positions: ");
		getContentPane().add(rotorPositionLabel, constraints);
		
		//sets up coord
		constraints.gridy=6;
		
		//adds ring position labels
		JLabel ringPositionLabel = new JLabel("Ring Positions: ");
		getContentPane().add(ringPositionLabel, constraints);
	}
	
	//sets up the buttons the increase the rotor positions
	private void addIncreaseRotorPosButtons(){
		//sets up the constraints
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=3;
		
		//adds an increase button for each rotor
		for(int i=0; i<3;i++){
			//creates the button
			JButton temp = new JButton("^");
			//sets the action command
			temp.setActionCommand("increase"+i);
			//sets the EnigmaGUI to be the action listener
			temp.addActionListener(this);
			
			//sets coords
			constraints.gridx=i+1;
			
			//removes extra padding for last button
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			//adds the button to the window
			getContentPane().add(temp, constraints);
		}
	}
	
	//add decrease rotor position buttons
	private void addDecreaseRotorPosButtons(){
		//sets up the constraints
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=5;
		
		//adds a decrease button for each rotor
		for(int i=0; i<3;i++){
			//creates a new button
			JButton temp = new JButton("v");
			//sets the action command
			temp.setActionCommand("decrease"+i);
			//adds the action listener
			temp.addActionListener(this);
			
			//sets up coords
			constraints.gridx=i+1;
			
			//the last button has the extra padding removed
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			//adds the button to the window
			getContentPane().add(temp, constraints);
		}
	}
	
	//sets up the settings button
	private void addSettingsButton(){
		//sets up the constraints
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=0;
		constraints.gridx=2;
		
		//creates a new button
		settingsButton= new JButton("Settings");
		//sets the action command
		settingsButton.setActionCommand("settings");
		//adds the action listener
		settingsButton.addActionListener(this);
		
		//adds the button to the window
		getContentPane().add(settingsButton, constraints);
	}
	
	//sets up the message input to the window
	private void addMessageInput(){
		//sets up a new textbox
		plainText = new JTextField(35);
		//sets the action command for the textbox
		plainText.setActionCommand("encrypt");
		//adds the listener
		plainText.addActionListener(this);
		
		//creates a label for the textbox
		JLabel plainTextLabel = new JLabel("message: ");
		
		//sets up the contraints for the label
		constraints.insets = new Insets(40,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=7;
		constraints.gridx=0;
		
		//adds the label to the window
		getContentPane().add(plainTextLabel, constraints);
		
		//sets up the constraints for the textbox
		constraints.insets = new Insets(40,3,3,3);		
		constraints.anchor = GridBagConstraints.LINE_START;		
		constraints.gridy=7;
		constraints.gridx=1;
		//the textbox spans 3 columns of the window grid
		constraints.gridwidth=3;
		
		//adds the textbox to the window
		getContentPane().add(plainText, constraints);
		//resets the constraints
		constraints.gridwidth=1;
		
	}
	
	//adds the box which displays the encrypted text
	private void addCipherTextOutput(){
		//creates a label for the encryption ouput
		JLabel cipherTextLabel = new JLabel("Encryption: ");
		//sets up the constraints for the label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=8;
		constraints.gridx=0;
		
		//adds the label to the window
		getContentPane().add(cipherTextLabel, constraints);
		
		//creates a new text area for outputing the encryption
		cipherText = new JTextArea(3,35);
		//sets it so that the text area isn't editable
		cipherText.setEditable(false);
		//sets it so that text that would be written outside the textbox, i.e. if a string is longer than the textbox, appears on a new line instead
		cipherText.setLineWrap(true);
		
		//makes it so that the textbox is scrollable
		JScrollPane cipherTextScrollPane = new JScrollPane(cipherText);
		//sets it so the scrollbar is always shown
		cipherTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//sets up the contraints
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=8;
		constraints.gridx=1;
		
		//sets it so that the text area spans 3 columns
		constraints.gridwidth=3;
		
		//adds the text area to the window
		getContentPane().add(cipherTextScrollPane, constraints);
		//resets the column span
		constraints.gridwidth=1;
	}
	
	//sets up the plugboard settings label
	private void addPlugBoardSettings(){
		//creates label to say what the plugboard settings label is outputting
		JLabel plugboardSettingsLabel = new JLabel("Plugboard Settings: ");
		
		//sets up the constraints
		constraints.insets = new Insets(40,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=9;
		constraints.gridx=0;
		
		//adds the label to the window
		getContentPane().add(plugboardSettingsLabel, constraints);
		
		//creates a label to display the plugboard settings
		plugboardSettings = new JLabel("Blah blah blah");
		//sets up the constraints
		constraints.insets = new Insets(40,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		
		//sets up the coords and column span
		constraints.gridy=9;
		constraints.gridx=1;
		constraints.gridwidth=3;
		
		//adds the label to the window
		getContentPane().add(plugboardSettings, constraints);
		//resets the column span
		constraints.gridwidth=1;
		
		//updates the label so it displays the correct settings
		updatePlugboard();
	}
	
	//method to encrypt input message
	public void encrypt(){
		//encrypts the inputted message and outputs it to cipherText
		cipherText.setText(enigma.encryptString(plainText.getText()));
		//updates the rotor positions
		updateRotorPos();
	}
	
	//method to open the settings menu
	public void openSettings(){
		//opens the settings menu
		SettingsGUI settings = new SettingsGUI(this);
		//makes this window non interactable when the settings menu is open
		this.setEnabled(false);
		
	}
	
	//method to increase the rotor positions
	public void increaseRotorPos(String actionCommand){
		//gets which rotor had its position increased
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		
		//gets that rotor from the scrambler
		Rotor rotor = (Rotor) enigma.getScrambler().getFirstRotor();
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		//gets the new position for that rotor
		char newPosition = (char)(((int)rotor.getCharOnTop()-96)%26 +97);
		//sets the new position for that rotor
		rotor.setPosition(newPosition);
		
		//update the rotor positions
		updateRotorPos();
	}
	
	//method to decrease the rotor positions
	public void decreaseRotorPos(String actionCommand){
		//gets which rotor had its position decreased
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		
		//gets that rotor from the scrambler
		Rotor rotor = (Rotor) enigma.getScrambler().getFirstRotor();
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		//gets the new position for that rotor
		char newPosition = (char)(((int)rotor.getCharOnTop()-72)%26 +97);
		//sets the new position for that rotor
		rotor.setPosition(newPosition);
		
		//updates the rotor positions
		updateRotorPos();
	}
	
	//method to updates the permutation output
	public void updatePermutation(){
		//gets the permutation from the enigma and splits it up
		String[] permutationArray = enigma.getPermutation().replaceAll("\\s","").split(",");
		
		//sets it so that the rotorNumberLabels displays the correct rotor number
		for(int i=1; i<4; i++){
			rotorNumberLabels.get(i-1).setText(permutationArray[i]);
		}
		
		//sets it so that the reflector label displays the correct reflector
		reflectorLabel.setText(permutationArray[0]);
	}
	
	//updates the rotor positions labels
	public void updateRotorPos(){
		//gets the scrambler from the enigma
		Scrambler scrambler=enigma.getScrambler();
		//gets the first rotor from the scrambler
		Rotor currentRotor = (Rotor) scrambler.getFirstRotor();
		
		//sets the rotor position labels to display each rotors current position
		for(int i=2;i>=0;i--){
			//sets the label i to display the rotors position
			rotorPositionLabels.get(i).setText(""+currentRotor.getCharOnTop());
			//gets the next rotor
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor = (Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	//updates the ring positions labels
	public void updateRingPos(){
		//gets the scrambler from the enigma
		Scrambler scrambler=enigma.getScrambler();
		//gets the first rotor from the scrambler
		Rotor currentRotor = (Rotor) scrambler.getFirstRotor();
		
		//sets the ring position labels to display each rotors ring position
		for(int i=2;i>=0;i--){
			//sets the label i to display the rotors ring position
			ringPositionLabels.get(i).setText(""+currentRotor.getRingPosition());
			//gets the next rotor
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor = (Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	//updates the plugboard setting labels
	public void updatePlugboard(){
		//gets the plugboard from the enigma machine
		Plugboard plugboard = enigma.getPlugboard();
		//gets the swap mapping from the plugboard
		ArrayList<String> mapping = plugboard.getSwapMapping();
		
		//if nothing has been swapped then no swaps is outputted
		if(mapping.isEmpty()){
			plugboardSettings.setText("No swaps");
		}
		//else the swap mapping is outputted
		else{
			plugboardSettings.setText(mapping.toString());
		}
	}
	
	//returns the EnigmaGUI's enigma machine
	public EnigmaMachine getEnigma(){
		return enigma;
	}
	
	//updates all the labels
	public void updateLabels(){
		updatePermutation();
		updatePlugboard();
		updateRingPos();
		updateRotorPos();
	}
	
	//this is the function that is run when an action is performed
	public void actionPerformed(ActionEvent e){
		//if the action's command is "encrypt" then the encrypt method is run
		if(e.getActionCommand().equals("encrypt")){
			encrypt();
		}
		//if the command is "settings" then the settings menu is opened
		else if(e.getActionCommand().equals("settings")){
			openSettings();
		}
		//if the command contains "increase" then the rotor positions are increased
		else if(e.getActionCommand().contains("increase")){
			increaseRotorPos(e.getActionCommand());
		}
		//if the command contains "decrease" then the rotor positions are decreased
		else if(e.getActionCommand().contains("decrease")){
			decreaseRotorPos(e.getActionCommand());
		}
	}
	
	//main method that opens up a new enigmaGUI
	public static void main(String[] Args){
		EnigmaGUI enigmaGUI = new EnigmaGUI();
	}
}