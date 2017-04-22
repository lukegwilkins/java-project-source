import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;

import enigmaComponents.*;

public class DecryptionGUI extends JFrame
					  implements ActionListener{
	//creates the variables for use in the program
	private JComboBox<String> candidateSettings;
	
	private JLabel rotorPositionLabel;
	private JLabel rotorPermutationLabel;
	private JLabel ringPositionLabel;
	private JLabel plugboardLabel;
	
	private JTextArea decryption;
	
	private JTextField turnoverPositionInput;
	private JTextField plugboardSwapInput;
	
	private GridBagConstraints constraints;
	
	private int height;
	private int width;
	
	private ArrayList<ArrayList<String>> candidateSettingsList;
	
	private ArrayList<String> settings;
	
	private String cipherText;
	
	private EnigmaMachine enigma;
	
	private BombeMachine bombe;
	
	//sets up the constructor for DecryptionGUI
	public DecryptionGUI(ArrayList<ArrayList<String>> candidateSettingsList, String cipherText, BombeMachine bombe){
		//starts the candidate settings, ciphertext and the Bombe
		this.candidateSettingsList = candidateSettingsList;
		this.cipherText=cipherText;
		this.bombe = bombe;
		
		height = 600;
		width = 600;
		
		//sets window title
		setTitle("Decryption GUI");
		//sets height and width
		setSize(width, height);
		//makes window visible
		setVisible(true);
		//makes the window non resizeable
		setResizable(false);
		//makes it so only this window closes when the window is closed
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//makes it so a grid layout is used
		getContentPane().setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints();
		
		//creates an enigma machine for use in the program
		enigma = new EnigmaMachine();
		
		//sets up the candidate settings dropdown
		setUpCandidateSettings();
		
		//sets up the labels for the enigma settings
		setUpRotorPermutationLabel();
		setUpRotorPosLabel();
		setUpRingPosLabel();
		setUpPlugboardLabel();
		
		//sets up the decryption output
		setUpDecryptionOutput();
		
		//sets the plugboard input and turnover input
		setUpPlugboardInput();
		setUpTurnoverInput();
		
		//sets up the reset button
		setUpResetButton();
		
		//sets the first candidate setting
		changeCandidateSettings();
		//packs everything so it is displayed to the window
		pack();
	}
	
	//method for setting up the candidate settings dropdown menu
	public void setUpCandidateSettings(){
		//creates an array to store the candidate settings
		String[] settings = new String[candidateSettingsList.size()];
		//adds the settings to the array
		for(int i=0; i<candidateSettingsList.size();i++){
			settings[i]=candidateSettingsList.get(i).toString();
		}
		
		//creates the dropdown menu for the settings
		candidateSettings = new JComboBox<String>(settings);
		candidateSettings.setSelectedIndex(0);
		//adds the action listener
		candidateSettings.addActionListener(this);
		//sets the action command
		candidateSettings.setActionCommand("candidateSettingsChanged");
		
		//sets the constraints
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		constraints.gridwidth = 2;
		
		//adds the dropdown menu to the window
		getContentPane().add(candidateSettings, constraints);
	}
	
	//sets up the rotor permutation label
	public void setUpRotorPermutationLabel(){
		//creates the label for the permuation and the permutation label label
		rotorPermutationLabel = new JLabel("Permutation");
		JLabel tempLabel = new JLabel("Permutation: ");
		constraints.gridwidth = 1;
		
		//sets the constraints for the permutation label label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 1;
		//adds the label to the window
		getContentPane().add(tempLabel, constraints);
		
		//sets the constraints for the permutation label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 1;
		//adds the label to the window
		getContentPane().add(rotorPermutationLabel, constraints);
	}
	
	//sets up the rotor position label
	public void setUpRotorPosLabel(){
		//creates the labels for the rotor position
		rotorPositionLabel = new JLabel("Hello");
		JLabel tempLabel = new JLabel("Rotor positions: ");
		constraints.gridwidth = 1;
		
		//sets the constraints for the rotor position label label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 2;
		//adds the label to the window
		getContentPane().add(tempLabel, constraints);
		
		//sets the constraints for the rotor position label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 2;
		
		//adds the label to the window
		getContentPane().add(rotorPositionLabel, constraints);
	}
	
	//sets up the ring position labels
	public void setUpRingPosLabel(){
		//creates the labels for the ring positions
		ringPositionLabel = new JLabel("Hello");
		JLabel tempLabel = new JLabel("Ring positions: ");
		constraints.gridwidth = 1;
		
		//sets the constraints for the ring position label label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 3;
		//adds the label to the window
		getContentPane().add(tempLabel, constraints);
		
		//sets the constraints for the ring position label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 3;
		//adds the label to the window
		getContentPane().add(ringPositionLabel, constraints);
	}
	
	//sets up the plugboard settings labels
	public void setUpPlugboardLabel(){
		//creates the labels
		plugboardLabel = new JLabel("plugboard");
		JLabel tempLabel = new JLabel("Plugboard Settings: ");
		constraints.gridwidth = 1;
		
		//sets up the constraints for the plugboard settings label label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 4;
		//adds the label to the window
		getContentPane().add(tempLabel, constraints);
		
		//sets the constraints for the plugboard settings label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 4;
		//adds the label to the window
		getContentPane().add(plugboardLabel, constraints);
	}
	
	//sets the decryption output
	public void setUpDecryptionOutput(){
		//sets up the decryption label
		JLabel tempLabel = new JLabel("Decryption: ");
		constraints.gridwidth = 2;
		
		//sets up the constraints for the label
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 5;
		//adds the label to the window
		getContentPane().add(tempLabel, constraints);
		
		//sets up the decryption output
		decryption = new JTextArea(5,44);
		decryption.setEditable(false);
		decryption.setLineWrap(true);
		
		//makes the output scrollable
		JScrollPane decryptionScrollPane = new JScrollPane(decryption);
		decryptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//sets up the constraints
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 6;
		
		//adds the output to the window
		getContentPane().add(decryptionScrollPane, constraints);
	}
	
	//sets up the plugboard input
	public void setUpPlugboardInput(){
		//creates a textbox to input settings
		plugboardSwapInput = new JTextField(20);
		
		//sets up the constraints for the textbox
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 7;
		
		//adds the textbox to the window
		getContentPane().add(plugboardSwapInput, constraints);
		
		//sets up the plugboard swap button
		JButton temp = new JButton("add plugboard swap");
		//sets the action for the button
		temp.setActionCommand("plugboardSwap");
		//sets the action listener
		temp.addActionListener(this);
		//sets up the constaints
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.LINE_END;
		
		//adds the button to the window
		getContentPane().add(temp, constraints);		
	}
	
	//sets up the turnover position input
	public void setUpTurnoverInput(){
		//sets up the textbox for input
		turnoverPositionInput = new JTextField(20);
		//sets the constraints
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 8;
		
		//adds the textbox to the window
		getContentPane().add(turnoverPositionInput, constraints);
		
		//creates a button to change the turnover pos
		JButton temp = new JButton("add turnover position");
		//sets the command
		temp.setActionCommand("turnover");
		//sets the action listener for the button
		temp.addActionListener(this);
		//sets the constraints
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.LINE_END;
		
		//adds the button to the window
		getContentPane().add(temp, constraints);
	}
	
	//sets up the reset button
	public void setUpResetButton(){
		//creates the button
		JButton resetButton = new JButton("Reset");
		//sets the command
		resetButton.setActionCommand("reset");
		//sets the action listener
		resetButton.addActionListener(this);
		
		//sets the constraints
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 9;
		
		constraints.gridwidth=2;
		
		//adds the button to the window
		getContentPane().add(resetButton, constraints);
	}
	
	//updates the rotor position label
	public void updateRotorPosition(){
		rotorPositionLabel.setText(settings.get(1));
	}
	
	//updates the permutation label
	public void updatePermutation(){
		rotorPermutationLabel.setText(settings.get(0));
	}
	
	//updates the ring position label
	public void updateRingPosition(){
		ringPositionLabel.setText(settings.get(3));
	}
	
	//updates the plugboard settings
	public void updatePlugboard(){
		plugboardLabel.setText(settings.get(2));
	}
	
	//changes which of the candidate settings we are using
	public void changeCandidateSettings(){
		//resets the settings and gets which of the candidate settings we are using
		settings = new ArrayList<String>(candidateSettingsList.get(candidateSettings.getSelectedIndex()));
		//resets the ring positions
		settings.add("1,1,1");
		//updates the output
		updateSettingsOutput();
		
	}
	
	//sets up the Enigma settings
	public void setUpEnigma(){
		setUpRotorPermutation();
		setUpRotorPositions();
		setUpRingPositions();
		setUpPlugboard();
	}
	
	//sets up the rorot permutation for the enigma machine
	public void setUpRotorPermutation(){
		enigma.setPermutation(settings.get(0));
	}
	
	//sets up the rotor positions for the enigma machine
	public void setUpRotorPositions(){
		//gets the position
		String[] rotorPositions = settings.get(1).split("");
		
		//gets the first rotor
		Rotor currentRotor = (Rotor) enigma.getScrambler().getFirstRotor();
		
		for(int i=2; i>=0; i--){
			//sets the rotor's position
			currentRotor.setPosition(rotorPositions[i].charAt(0));
			//gets the next rotor 
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor =(Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	//sets up the rotor's ring position
	public void setUpRingPositions(){
		//gets the ring position
		String[] ringPositions = settings.get(3).split(",");
		//gets the first rotor
		Rotor currentRotor = (Rotor) enigma.getScrambler().getFirstRotor();
		
		for(int i=2; i>=0; i--){
			//sets the rotor's position
			currentRotor.setRingPosition(Integer.parseInt(ringPositions[i]));
			//gets the next rotor 
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor =(Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	//sets the plugboard settings for the enigma
	public void setUpPlugboard(){
		//gets the plugboard settings and converts it to an arraylist
		String[] swaps = settings.get(2).replaceAll("\\s","").split(",");
		ArrayList<String> mapping = new ArrayList<String>(Arrays.asList(swaps));
		//sets the plugboard settings in the enigma
		enigma.getPlugboard().setSwapMapping(mapping);
	}
	
	//method to update the decryption output
	public void updateDecryption(){
		//sets the rotor positions
		setUpRotorPositions();
		//updates the decryption output
		decryption.setText(enigma.encryptString(cipherText));
	}
	
	//updates the settings output
	public void updateSettingsOutput(){
		updateRotorPosition();
		updatePermutation();
		updateRingPosition();
		updatePlugboard();
		
		setUpEnigma();
		
		updateDecryption();
	}
	
	//adds a swap to the plugboard settings
	public void addPlugboardSwap(){
		//gets the swaps to the plugboard settings
		settings.set(2,(settings.get(2)+", "+plugboardSwapInput.getText()));
		//updates the settings output and decryption
		updateSettingsOutput();
	}
	
	//method to change the rotor position and ring position to cause a turnover position
	public void addTurnoverPosition(){
		//gets the turnover position and which rotor turned over then
		String[] turnoverInput = turnoverPositionInput.getText().split(",");
		int turnover = Integer.parseInt(turnoverInput[0]);
		int rotorPermPos = Integer.parseInt(turnoverInput[1]);
		
		//gets the permutation
		String[] perm = settings.get(0).replaceAll("\\s+","").split(",");
		int rotorNum = Integer.parseInt(perm[4-rotorPermPos]);
		
		//char newStart = bombe.startPosForTurnover(rotorNum, turnover);
		
		//sets up the string to store the new start position
		String newStartPos = "";
		
		for(int i=1; i<=rotorPermPos; i++){
			//gets the number of the ith rotor starting from the right
			//(list is length 4 so the right rotor is at index 3 in the list, so we do 4-1 as it is the 1st rotor to offset the 1)
			rotorNum = Integer.parseInt(perm[4-i]);
			//adds the new start position to newStartPos
			newStartPos=bombe.startPosForTurnover(rotorNum, turnover)+newStartPos;
			//divides the turnover by 26 and rounds up
			turnover=(int)Math.ceil(((double)turnover)/26.0);
		}
		
		//the other rotor's don't have a change so we just get their current positions and add them to newStartPos
		String temp ="";
		for(int i=0; i<3-rotorPermPos; i++){
			temp = temp + settings.get(1).charAt(i);
		}
		
		newStartPos = temp + newStartPos;
		
		//System.out.println(newStartPos);
		//System.out.println(newStart);
		
		//gets the current starting pos
		String currentStartPos = settings.get(1);
		
		//gets and sets the new ring settings
		settings.set(3,bombe.adjustStartPos(currentStartPos, settings.get(3), newStartPos));
		
		//sets the new starting positions
		settings.set(1, newStartPos);
		
		//updates the settings and decryption output
		updateSettingsOutput();
	}
	
	//resets the candidate settings
	public void reset(){
		changeCandidateSettings();
	}
	
	//method to handle actions performed
	public void actionPerformed(ActionEvent e){
		//if a new candidate settings was selected from the drop down then the changeCandidateSettings method is run
		if(e.getActionCommand().equals("candidateSettingsChanged")){
			changeCandidateSettings();
		}
		//if the add plugboard swap button is pressed then we call the addPlugboardSwap method
		else if(e.getActionCommand().equals("plugboardSwap")){
			addPlugboardSwap();
		}
		//if a turnover position was inputted then we call the addTurnoverPos method
		else if(e.getActionCommand().equals("turnover")){
			addTurnoverPosition();
		}
		//if the reset button is pressed then the reset method is called
		else if(e.getActionCommand().equals("reset")){
			reset();
		}
	}
}