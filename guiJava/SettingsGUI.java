import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import enigmaComponents.*;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsGUI extends JFrame implements ActionListener{
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
		height = 600;
		width = 600;
		buttonPadding = 40;
		
		setTitle("Settings Menu");
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.parentWindow = parentWindow;
		
		setUpCloseOperation();
		
		getContentPane().setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints();
		
		ringPositionLabels = new ArrayList<JLabel>();
		
		rotorMenus = new ArrayList<JComboBox<String>>();
		setUpRingPositions();
		
		setUpPermutationOptions();
		//getContentPane().add(deleteMeLater);
		
		setUpPlugboardOptions();
		
		pack();
	}
	
	private void setUpRingLabels(){
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridy=2;
		
		JLabel ringPositionsLabel = new JLabel("Ring Positions: ");
		getContentPane().add(ringPositionsLabel, constraints);
		
		constraints.insets = new Insets(3,3,3,buttonPadding);
		constraints.anchor=GridBagConstraints.CENTER;
		for(int i=0; i<3; i++){
			constraints.gridx=i+1;
			
			JLabel temp = new JLabel(""+i);
			ringPositionLabels.add(temp);
			
			/*if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}*/
			
			getContentPane().add(temp, constraints);
		}		
			
	}
	
	private void setUpRingPositions(){
		setUpRingLabels();
		setUpRingIncreaseButtons();
		setUpRingDecreaseButtons();
		updateRingPositions();
	}
	
	private void setUpRingIncreaseButtons(){
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=1;
		
		for(int i=0; i<3;i++){
			JButton temp = new JButton("^");
			temp.setActionCommand("increaseRing"+i);
			temp.addActionListener(this);
			
			constraints.gridx=i+1;
			
			//increaseRotorPosButtons.add(temp);
			
			/*if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}*/
			
			getContentPane().add(temp, constraints);
		}
	}
	
	private void setUpRingDecreaseButtons(){
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=3;
		
		for(int i=0; i<3;i++){
			JButton temp = new JButton("v");
			temp.setActionCommand("decreaseRing"+i);
			temp.addActionListener(this);
			
			constraints.gridx=i+1;
			
			//increaseRotorPosButtons.add(temp);
			
			/*if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}*/
			
			getContentPane().add(temp, constraints);
		}
	}
	
	private void updateRingPositions(){
		Scrambler scrambler=parentWindow.getEnigma().getScrambler();
		Rotor currentRotor = (Rotor) scrambler.getFirstRotor();
		
		for(int i=2;i>=0;i--){
			ringPositionLabels.get(i).setText(""+currentRotor.getRingPosition());
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor = (Rotor) currentRotor.getNextRotor();
			}
		}
		
		parentWindow.updateRingPos();
	}
	
	public void increaseRingPos(String actionCommand){
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		Rotor rotor = (Rotor) parentWindow.getEnigma().getScrambler().getFirstRotor();
		
		//System.out.println(rotorNum);
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		int newRingPos = (rotor.getRingPosition()+1)%26;
		
		if(newRingPos==0){
			newRingPos=1;
		}
		
		rotor.setRingPosition(newRingPos);
		updateRingPositions();
	}
	
	public void decreaseRingPos(String actionCommand){
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		Rotor rotor = (Rotor) parentWindow.getEnigma().getScrambler().getFirstRotor();
		
		//System.out.println(rotorNum);
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		int newRingPos = (25+rotor.getRingPosition())%26;
		
		if(newRingPos==0){
			newRingPos=26;
		}
		
		rotor.setRingPosition(newRingPos);
		updateRingPositions();		
	}
	
	private void setUpPermutationOptions(){
		setUpPermutationLabel();
		
		setUpReflectorMenu();
		setUpPermutationButton();
		
		setUpRotorMenus();
		
		setUpPermMenuSelect();
	}
	
	private void setUpPermutationLabel(){
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridy=0;
		constraints.gridx=0;
		
		JLabel temp = new JLabel("Permutation: ");
		
		getContentPane().add(temp,constraints);
	}
	
	private void setUpReflectorMenu(){
		String[] reflectors ={"A","B","C"};
		reflectorMenu = new JComboBox<String>(reflectors);
		
		reflectorMenu.setSelectedIndex(0);
		
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=0;
		constraints.gridx=1;
		
		getContentPane().add(reflectorMenu,constraints);
	}
	
	private void setUpRotorMenus(){
		String[] rotors ={"1","2","3","4","5"};
		constraints.insets = new Insets(3,3,3,buttonPadding);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=0;
		
		for(int i=0; i<3; i++){
			JComboBox<String> temp = new JComboBox<String>(rotors);
			constraints.gridx=i+2;
			
			rotorMenus.add(temp);
			getContentPane().add(temp, constraints);
		}
		
	}
	
	private void setUpPermMenuSelect(){
		String permutation = parentWindow.getEnigma().getPermutation().replaceAll("\\s","");
		String[] settings = permutation.split(",");
		
		int reflectorIndex = ((int)settings[0].toLowerCase().charAt(0))-97;
		//System.out.println(reflectorIndex);
		reflectorMenu.setSelectedIndex(reflectorIndex);
		
		for(int i=1; i<4; i++){
			int index = Integer.parseInt(settings[i])-1;
			rotorMenus.get(i-1).setSelectedIndex(index);
		}
	}
	
	private void setUpPermutationButton(){
		JButton permutationButton = new JButton("change permutation");
		permutationButton.setActionCommand("changePerm");
		permutationButton.addActionListener(this);
		
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=0;
		constraints.gridx=5;
		
		
		getContentPane().add(permutationButton, constraints);
		
	}
	
	private void changePermutation(){
		String permutation = (String)reflectorMenu.getSelectedItem();
		
		for(int i=0; i<3; i++){
			permutation = permutation +"," +(String)rotorMenus.get(i).getSelectedItem();
		}
		//System.out.println(permutation);
		parentWindow.getEnigma().setPermutation(permutation);
		
		parentWindow.updatePermutation();
	}
	
	private void setUpCloseOperation(){
		WindowListener exitListener = new WindowAdapter(){
			
			@Override
			public void windowClosing(WindowEvent e){
				//System.out.println("hello");
				parentWindow.setEnabled(true);
				setVisible(false);
				dispose();
			}
		};
		addWindowListener(exitListener);
	}
	
	private void setUpPlugboardOptions(){
		setUpPlugboardInput();
		setUpPlugboardButton();
	}
	
	private void setUpPlugboardInput(){
		JLabel plugboardLabel = new JLabel("plugboard settings:");
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridy=4;
		constraints.gridx=0;
		
		getContentPane().add(plugboardLabel, constraints);
		
		plugboardInput = new JTextField(25);
		
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=4;
		constraints.gridx=1;
		constraints.gridwidth=4;
		
		getContentPane().add(plugboardInput, constraints);
	}
	
	private void setUpPlugboardButton(){
		JButton plugboardButton = new JButton("change settings");
		plugboardButton.setActionCommand("changePlug");
		plugboardButton.addActionListener(this);
		
		constraints.insets = new Insets(3,3,3,3);			
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=4;
		constraints.gridx=5;
		constraints.gridwidth=1;
		
		getContentPane().add(plugboardButton, constraints);
		
	}
	
	private void changePlugboardSettings(){
		String[] swaps = plugboardInput.getText().split(",");
		ArrayList<String> mapping = new ArrayList<String>(Arrays.asList(swaps));
		System.out.println(mapping);
		parentWindow.getEnigma().getPlugboard().setSwapMapping(mapping);
		
		parentWindow.updatePlugboard();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().contains("increaseRing")){
			increaseRingPos(e.getActionCommand());
		}
		else if(e.getActionCommand().contains("decreaseRing")){
			decreaseRingPos(e.getActionCommand());
		}
		else if(e.getActionCommand().equals("changePerm")){
			changePermutation();
		}
		else if(e.getActionCommand().equals("changePlug")){
			changePlugboardSettings();
		}
	}
}