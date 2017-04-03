import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import enigmaComponents.*;

import java.util.ArrayList;

public class EnigmaGUI extends JFrame implements ActionListener{
	private ArrayList<JLabel> rotorPositionLabels;
	private ArrayList<JLabel> rotorNumberLabels;
	private ArrayList<JLabel> ringPositionLabels;
	
	private JLabel plugboardSettings;
	
	//private ArrayList<JButton> increaseRotorPosButtons;
	//private ArrayList<JButton> decreaseRotorPosButtons;
	
	private JButton settingsButton;
	
	private JTextField plainText;
	private JTextArea cipherText;
	
	private int height;
	private int width;
	
	private int buttonPadding=100;
	private GridBagConstraints constraints;
	
	private EnigmaMachine enigma;
	
	private JLabel reflectorLabel;
	
	public EnigmaGUI(){
		enigma = new EnigmaMachine();
		
		height=600;
		width=600;
		setTitle("Enigma GUI");
		setSize(width, height);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//set layout to be a grid bag layout
		getContentPane().setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints();
		
		rotorPositionLabels = new ArrayList<JLabel>();
		rotorNumberLabels = new ArrayList<JLabel>();
		ringPositionLabels = new ArrayList<JLabel>();
		
		settingsLabels();
		
		addRotorNumberLabels();
		addRotorPositionLabels();
		addRingPositionLabels();
		
		//increaseRotorPosButtons = new ArrayList<JButton>();
		//decreaseRotorPosButtons = new ArrayList<JButton>();
		
		addIncreaseRotorPosButtons();
		addDecreaseRotorPosButtons();
		
		addSettingsButton();
		
		addMessageInput();
		addCipherTextOutput();
		
		addPlugBoardSettings();
		
		pack();
	}
	
	private void addRotorNumberLabels(){
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx=0;
		constraints.gridy=1;
		
		JLabel reflectorOutputLabel = new JLabel("Reflector:");
		getContentPane().add(reflectorOutputLabel, constraints);
		
		constraints.gridx=2;
		reflectorLabel = new JLabel("Test");
		
		
		getContentPane().add(reflectorLabel,constraints);
		
		constraints.gridy=2;
		for(int i=0; i<3; i++){
			constraints.gridx=i+1;
			
			JLabel temp = new JLabel(""+(i+5));
			rotorNumberLabels.add(temp);
			
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			getContentPane().add(temp, constraints);
		}
		
		updatePermutation();
	}
	
	private void addRotorPositionLabels(){
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=4;
		for(int i=0; i<3; i++){
			constraints.gridx=i+1;
			
			JLabel temp = new JLabel(""+i);
			rotorPositionLabels.add(temp);
			
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			getContentPane().add(temp, constraints);
		}		
		
		updateRotorPos();
	}
	
	private void addRingPositionLabels(){
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=6;
		for(int i=0; i<3; i++){
			constraints.gridx=i+1;
			
			JLabel temp = new JLabel(""+(i+7));
			ringPositionLabels.add(temp);
			
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			getContentPane().add(temp, constraints);
		}
		
		updateRingPos();
	}
	
	private void settingsLabels(){
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=2;
		constraints.gridx=0;
		
		JLabel rotorNumberLabel = new JLabel("Rotor Numbers: ");
		getContentPane().add(rotorNumberLabel, constraints);
		
		constraints.gridy=4;
		
		JLabel rotorPositionLabel = new JLabel("Rotor Positions: ");
		getContentPane().add(rotorPositionLabel, constraints);
		
		constraints.gridy=6;
		
		JLabel ringPositionLabel = new JLabel("Ring Positions: ");
		getContentPane().add(ringPositionLabel, constraints);
	}
	
	private void addIncreaseRotorPosButtons(){
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=3;
		
		for(int i=0; i<3;i++){
			JButton temp = new JButton("^");
			temp.setActionCommand("increase"+i);
			temp.addActionListener(this);
			
			constraints.gridx=i+1;
			
			//increaseRotorPosButtons.add(temp);
			
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			getContentPane().add(temp, constraints);
		}
	}
	
	private void addDecreaseRotorPosButtons(){
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=5;
		
		for(int i=0; i<3;i++){
			JButton temp = new JButton("v");
			temp.setActionCommand("decrease"+i);
			temp.addActionListener(this);
			
			constraints.gridx=i+1;
			
			//decreaseRotorPosButtons.add(temp);
			
			if(i==2){
				constraints.insets = new Insets(3,3,3,3);
			}
			
			getContentPane().add(temp, constraints);
		}
	}
	
	private void addSettingsButton(){
		constraints.insets = new Insets(3,3,3, buttonPadding);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy=0;
		constraints.gridx=2;
		
		settingsButton= new JButton("Settings");
		settingsButton.setActionCommand("settings");
		settingsButton.addActionListener(this);
		
		getContentPane().add(settingsButton, constraints);
	}
	
	private void addMessageInput(){
		plainText = new JTextField(35);
		plainText.setActionCommand("encrypt");
		plainText.addActionListener(this);
		
		JLabel plainTextLabel = new JLabel("message: ");
		
		constraints.insets = new Insets(40,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=7;
		constraints.gridx=0;
		
		getContentPane().add(plainTextLabel, constraints);
		
		//JScrollPane plainTextScrollPane = new JScrollPane(plainText);
		//plainTextScrollPane.setPreferredSize(new Dimension(250, 100));
		
		constraints.insets = new Insets(40,3,3,3);		
		constraints.anchor = GridBagConstraints.LINE_START;		
		constraints.gridy=7;
		constraints.gridx=1;
		constraints.gridwidth=3;
		
		getContentPane().add(plainText, constraints);
		constraints.gridwidth=1;
		
	}
	
	private void addCipherTextOutput(){
		JLabel cipherTextLabel = new JLabel("Encryption: ");
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=8;
		constraints.gridx=0;
		
		getContentPane().add(cipherTextLabel, constraints);
		
		cipherText = new JTextArea(3,35);
		cipherText.setEditable(false);
		cipherText.setLineWrap(true);
		
		JScrollPane cipherTextScrollPane = new JScrollPane(cipherText);
		cipherTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=8;
		constraints.gridx=1;
		constraints.gridwidth=3;
		
		getContentPane().add(cipherTextScrollPane, constraints);
		constraints.gridwidth=1;
	}
	
	private void addPlugBoardSettings(){
		JLabel plugboardSettingsLabel = new JLabel("Plugboard Settings: ");
		constraints.insets = new Insets(40,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridy=9;
		constraints.gridx=0;
		
		getContentPane().add(plugboardSettingsLabel, constraints);
		
		plugboardSettings = new JLabel("Blah blah blah");
		constraints.insets = new Insets(40,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		
		constraints.gridy=9;
		constraints.gridx=1;
		constraints.gridwidth=3;
		
		getContentPane().add(plugboardSettings, constraints);
		constraints.gridwidth=1;
		
		updatePlugboard();
	}
	
	public void encrypt(){
		//System.out.println(plainText.getText());
		cipherText.setText(enigma.encryptString(plainText.getText()));
		updateRotorPos();
	}
	
	public void openSettings(){		
		SettingsGUI settings = new SettingsGUI(this);		
		this.setEnabled(false);
		
	}
	
	public void increaseRotorPos(String actionCommand){
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		Rotor rotor = (Rotor) enigma.getScrambler().getFirstRotor();
		
		//System.out.println(rotorNum);
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		char newPosition = (char)(((int)rotor.getCharOnTop()-96)%26 +97);
		rotor.setPosition(newPosition);
		//System.out.println("increase rotor at position: "+actionCommand.charAt(actionCommand.length()-1));
		updateRotorPos();
	}
	
	public void decreaseRotorPos(String actionCommand){
		int rotorNum = Integer.parseInt(""+actionCommand.charAt(actionCommand.length()-1));
		Rotor rotor = (Rotor) enigma.getScrambler().getFirstRotor();
		
		//System.out.println(rotorNum);
		for(int i=0; i<2-rotorNum;i++){
			rotor =(Rotor) rotor.getNextRotor();
		}
		
		char newPosition = (char)(((int)rotor.getCharOnTop()-72)%26 +97);
		rotor.setPosition(newPosition);
		//System.out.println("increase rotor at position: "+actionCommand.charAt(actionCommand.length()-1));
		updateRotorPos();
	}
	
	public void updatePermutation(){
		String[] permutationArray = enigma.getPermutation().replaceAll("\\s","").split(",");
		
		for(int i=1; i<4; i++){
			rotorNumberLabels.get(i-1).setText(permutationArray[i]);
		}
		
		reflectorLabel.setText(permutationArray[0]);
		//System.out.println(enigma.getPermutation());
	}
	
	public void updateRotorPos(){
		Scrambler scrambler=enigma.getScrambler();
		Rotor currentRotor = (Rotor) scrambler.getFirstRotor();
		
		for(int i=2;i>=0;i--){
			rotorPositionLabels.get(i).setText(""+currentRotor.getCharOnTop());
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor = (Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	public void updateRingPos(){
		Scrambler scrambler=enigma.getScrambler();
		Rotor currentRotor = (Rotor) scrambler.getFirstRotor();
		
		for(int i=2;i>=0;i--){
			ringPositionLabels.get(i).setText(""+currentRotor.getRingPosition());
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor = (Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	public void updatePlugboard(){
		Plugboard plugboard = enigma.getPlugboard();
		ArrayList<String> mapping = plugboard.getSwapMapping();
		if(mapping.isEmpty()){
			plugboardSettings.setText("No swaps");
		}
		else{
			plugboardSettings.setText(mapping.toString());
		}
	}
	
	public EnigmaMachine getEnigma(){
		return enigma;
	}
	
	public void updateLabels(){
		updatePermutation();
		updatePlugboard();
		updateRingPos();
		updateRotorPos();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("encrypt")){
			encrypt();
		}
		else if(e.getActionCommand().equals("settings")){
			openSettings();
		}
		else if(e.getActionCommand().contains("increase")){
			increaseRotorPos(e.getActionCommand());
		}
		else if(e.getActionCommand().contains("decrease")){
			decreaseRotorPos(e.getActionCommand());
		}
	}
	
	public static void main(String[] Args){
		//java.awt.EventQueue.invokeLater(new Runnable(){
				//public void run(){
		EnigmaGUI enigmaGUI = new EnigmaGUI();
		//		}
		//	}
		//);
	}
}