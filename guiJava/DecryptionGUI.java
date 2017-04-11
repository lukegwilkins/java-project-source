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
	
	public DecryptionGUI(ArrayList<ArrayList<String>> candidateSettingsList, String cipherText, BombeMachine bombe){
		this.candidateSettingsList = candidateSettingsList;
		this.cipherText=cipherText;
		this.bombe = bombe;
		
		height = 600;
		width = 600;
		
		setTitle("Decryption GUI");
		setSize(width, height);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints();
		
		enigma = new EnigmaMachine();
		
		setUpCandidateSettings();
		
		setUpRotorPermutationLabel();
		setUpRotorPosLabel();
		setUpRingPosLabel();
		setUpPlugboardLabel();
		
		setUpDecryptionOutput();
		
		setUpPlugboardInput();
		setUpTurnoverInput();
		
		setUpResetButton();
		
		changeCandidateSettings();
		pack();
	}
	
	public void setUpCandidateSettings(){
		String[] settings = new String[candidateSettingsList.size()];
		for(int i=0; i<candidateSettingsList.size();i++){
			settings[i]=candidateSettingsList.get(i).toString();
		}
		//String[] settings ={"dog","cat","fish"};
		
		candidateSettings = new JComboBox<String>(settings);
		candidateSettings.setSelectedIndex(0);
		candidateSettings.addActionListener(this);
		candidateSettings.setActionCommand("candidateSettingsChanged");
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		constraints.gridwidth = 2;
		
		getContentPane().add(candidateSettings, constraints);
	}
	
	public void setUpRotorPermutationLabel(){
		rotorPermutationLabel = new JLabel("Permutation");
		JLabel tempLabel = new JLabel("Permutation: ");
		constraints.gridwidth = 1;
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 1;
		getContentPane().add(tempLabel, constraints);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 1;
		getContentPane().add(rotorPermutationLabel, constraints);
	}
	
	public void setUpRotorPosLabel(){
		rotorPositionLabel = new JLabel("Hello");
		JLabel tempLabel = new JLabel("Rotor positions: ");
		constraints.gridwidth = 1;
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 2;
		getContentPane().add(tempLabel, constraints);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 2;
		
		getContentPane().add(rotorPositionLabel, constraints);
	}
	
	public void setUpRingPosLabel(){
		ringPositionLabel = new JLabel("Hello");
		JLabel tempLabel = new JLabel("Ring positions: ");
		constraints.gridwidth = 1;
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 3;
		getContentPane().add(tempLabel, constraints);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 3;
		getContentPane().add(ringPositionLabel, constraints);
	}
	
	public void setUpPlugboardLabel(){
		plugboardLabel = new JLabel("plugboard");
		JLabel tempLabel = new JLabel("Plugboard Settings: ");
		constraints.gridwidth = 1;
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 4;
		getContentPane().add(tempLabel, constraints);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 4;
		getContentPane().add(plugboardLabel, constraints);
	}
	
	public void setUpDecryptionOutput(){
		JLabel tempLabel = new JLabel("Decyption: ");
		constraints.gridwidth = 2;
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 5;
		getContentPane().add(tempLabel, constraints);
		
		decryption = new JTextArea(5,44);
		decryption.setEditable(false);
		decryption.setLineWrap(true);
		
		JScrollPane decryptionScrollPane = new JScrollPane(decryption);
		decryptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 6;
		
		getContentPane().add(decryptionScrollPane, constraints);
	}
	
	public void setUpPlugboardInput(){
		plugboardSwapInput = new JTextField(20);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 7;
		
		getContentPane().add(plugboardSwapInput, constraints);
		
		JButton temp = new JButton("add plugboard swap");
		temp.setActionCommand("plugboardSwap");
		temp.addActionListener(this);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.LINE_END;
		
		getContentPane().add(temp, constraints);		
	}
	
	public void setUpTurnoverInput(){
		turnoverPositionInput = new JTextField(20);
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = 0;
		constraints.gridy = 8;
		
		getContentPane().add(turnoverPositionInput, constraints);
		
		JButton temp = new JButton("add turnover position");
		temp.setActionCommand("turnover");
		temp.addActionListener(this);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.LINE_END;
		
		getContentPane().add(temp, constraints);
	}
	
	public void setUpResetButton(){
		JButton resetButton = new JButton("Reset");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		
		constraints.insets = new Insets(3,3,3,3);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 9;
		
		constraints.gridwidth=2;
		
		getContentPane().add(resetButton, constraints);
	}
	
	public void updateRotorPosition(){
		rotorPositionLabel.setText(settings.get(1));
	}
	
	public void updatePermutation(){
		rotorPermutationLabel.setText(settings.get(0));
	}
	
	public void updateRingPosition(){
		ringPositionLabel.setText(settings.get(3));
	}
	
	public void updatePlugboard(){
		plugboardLabel.setText(settings.get(2));
	}
	
	public void changeCandidateSettings(){
		settings = new ArrayList<String>(candidateSettingsList.get(candidateSettings.getSelectedIndex()));
		settings.add("1,1,1");
		updateSettingsOutput();
		
	}
	
	public void setUpEnigma(){
		setUpRotorPermutation();
		setUpRotorPositions();
		setUpRingPositions();
		setUpPlugboard();
	}
	
	public void setUpRotorPermutation(){
		enigma.setPermutation(settings.get(0));
	}
	
	public void setUpRotorPositions(){
		String[] rotorPositions = settings.get(1).split("");
		
		Rotor currentRotor = (Rotor) enigma.getScrambler().getFirstRotor();
		
		for(int i=2; i>=0; i--){
			currentRotor.setPosition(rotorPositions[i].charAt(0));
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor =(Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	public void setUpRingPositions(){
		String[] ringPositions = settings.get(3).split(",");
		
		Rotor currentRotor = (Rotor) enigma.getScrambler().getFirstRotor();
		
		for(int i=2; i>=0; i--){
			currentRotor.setRingPosition(Integer.parseInt(ringPositions[i]));
			if(currentRotor.getNextRotor() instanceof Rotor){
				currentRotor =(Rotor) currentRotor.getNextRotor();
			}
		}
	}
	
	public void setUpPlugboard(){
		String[] swaps = settings.get(2).replaceAll("\\s","").split(",");
		ArrayList<String> mapping = new ArrayList<String>(Arrays.asList(swaps));
		//System.out.println(mapping);
		enigma.getPlugboard().setSwapMapping(mapping);
	}
	
	public void updateDecryption(){
		setUpRotorPositions();
		
		decryption.setText(enigma.encryptString(cipherText));
		//setUpEnigma();
	}
	
	public void updateSettingsOutput(){
		updateRotorPosition();
		updatePermutation();
		updateRingPosition();
		updatePlugboard();
		
		setUpEnigma();
		
		updateDecryption();
	}
	
	public void addPlugboardSwap(){
		//System.out.println(plugboardSwapInput.getText()+"plugboard");
		settings.set(2,(settings.get(2)+", "+plugboardSwapInput.getText()));
		updateSettingsOutput();
	}
	
	public void addTurnoverPosition(){
		String[] turnoverInput = turnoverPositionInput.getText().split(",");
		int turnover = Integer.parseInt(turnoverInput[0]);
		int rotorPermPos = Integer.parseInt(turnoverInput[1]);
		
		String[] perm = settings.get(0).replaceAll("\\s+","").split(",");
		int rotorNum = Integer.parseInt(perm[4-rotorPermPos]);
		
		char newStart = bombe.startPosForTurnover(rotorNum, turnover);
		
		String newStartPos = "";
		
		for(int i=1; i<=rotorPermPos; i++){
			rotorNum = Integer.parseInt(perm[4-i]);
			newStartPos=bombe.startPosForTurnover(rotorNum, turnover)+newStartPos;
			turnover=(int)Math.ceil(((double)turnover)/26.0);
		}
		
		String temp ="";
		for(int i=0; i<3-rotorPermPos; i++){
			temp = temp + settings.get(1).charAt(i);
		}
		
		newStartPos = temp + newStartPos;
		
		System.out.println(newStartPos);
		//System.out.println(newStart);
		String currentStartPos = settings.get(1);
		
		//String newStartPos = currentStartPos.substring(0, (3-rotorPermPos))+newStart+currentStartPos.substring(4-rotorPermPos,currentStartPos.length());
		settings.set(3,bombe.adjustStartPos(currentStartPos, settings.get(3), newStartPos));
		
		settings.set(1, newStartPos);
		
		//settings.set(2,(settings.get(2)+"turnover"));
		updateSettingsOutput();
	}
	
	public void reset(){
		changeCandidateSettings();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("candidateSettingsChanged")){
			changeCandidateSettings();
		}
		else if(e.getActionCommand().equals("plugboardSwap")){
			addPlugboardSwap();
		}
		else if(e.getActionCommand().equals("turnover")){
			addTurnoverPosition();
		}
		else if(e.getActionCommand().equals("reset")){
			reset();
		}
	}
	
	/*public static void main(String[] Args){
		DecryptionGUI gui = new DecryptionGUI();
	}*/
}