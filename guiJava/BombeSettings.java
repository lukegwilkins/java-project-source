import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.*;

public class BombeSettings extends JFrame
						   implements ActionListener{
	private int height;
	private int width;
	
	private GridBagConstraints constraints;
	
	private JTextField noOfClosuresInput;
	
	private BombeGUI bombeGUI;
	
	private JLabel noOfClosureLabel;
	
	private ArrayList<JCheckBox> reflectors;
	
	public BombeSettings(BombeGUI parentWindow){
		height=600;
		width=600;
		setTitle("Bombe Settings");
		setSize(width, height);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		bombeGUI=parentWindow;
		//set layout to be a grid bag layout
		getContentPane().setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints();
		//want everything to be to the left
		//constraints.anchor = GridBagConstraints.WEST;
		//how much padding to put at top, left, right and bottom (might not be that order in constructored)
		constraints.insets = new Insets(3,3,3,3);
		
		setupNoOfClosuresInput();
		setupNoOfClosuresButton();
		setupNoOfClosureLabel();
		
		setupReflectorsInput();
		changeReflectors();
		pack();
	}
	
	public void setupNoOfClosuresInput(){
		constraints.gridx=0;
		constraints.gridy=3;
		constraints.gridwidth=2;
		noOfClosuresInput = new JTextField(15);
		getContentPane().add(noOfClosuresInput, constraints);
		constraints.gridwidth=1;
	}
	
	public void setupNoOfClosuresButton(){
		JButton button = new JButton("Change no of closures");
		
		button.setVerticalTextPosition(AbstractButton.CENTER);
		button.setHorizontalTextPosition(AbstractButton.LEADING);
		button.setMnemonic(KeyEvent.VK_D);
		button.setActionCommand("changeClosureNo");
		
		button.addActionListener(this);
		
		constraints.gridx=2;
		constraints.gridy=3;
		
		getContentPane().add(button, constraints);
	}
	
	public void setupNoOfClosureLabel(){
		noOfClosureLabel = new JLabel();
		constraints.gridx=0;
		constraints.gridy=2;
		
		getContentPane().add(noOfClosureLabel, constraints);
		
		updateNoOfClosureLabel();
	}
	
	public void updateNoOfClosureLabel(){
		noOfClosureLabel.setText("Number of Closures used is: "+bombeGUI.getNumOfClosures());
	}
	
	public void changeClosureNo(){
		bombeGUI.setNumOfClosures(Integer.parseInt(noOfClosuresInput.getText()));
		updateNoOfClosureLabel();
	}
	
	public void setupReflectorsInput(){
		constraints.gridy=0;
		constraints.gridx=1;
		JLabel reflectorLabel = new JLabel("Reflectors");
		
		getContentPane().add(reflectorLabel, constraints);
		
		constraints.gridy=1;
		reflectors = new ArrayList<JCheckBox>();
		
		for(int i=0;i<3;i++){
			JCheckBox temp = new JCheckBox(""+((char)(97+i)));
			temp.setMnemonic(KeyEvent.VK_C);
			temp.setSelected(true);
			temp.setActionCommand("reflector");
			
			temp.addActionListener(this);
			constraints.gridx=i;
			
			getContentPane().add(temp, constraints);
			reflectors.add(temp);
		}
		
		updateReflectorCheckBoxes();
	}
	
	public void updateReflectorCheckBoxes(){
		ArrayList<Character> useableReflectors = bombeGUI.getBombe().getUsableReflectors();
		
		for(int i=0; i<3;i++){
			if(useableReflectors.contains((char)(97+i))){
				reflectors.get(i).setSelected(true);
			}
			else{
				reflectors.get(i).setSelected(false);
			}
		}
	}
	
	
	public void changeReflectors(){
		ArrayList<Character> useableReflectors = new ArrayList<Character>();
		for(int i=0;i<3;i++){
			if(reflectors.get(i).isSelected()){
				useableReflectors.add((char)(97+i));
			}
		}
		bombeGUI.getBombe().setUseableReflectors(useableReflectors);
		
		if(useableReflectors.size() <=1){
			disableSelectedReflectors();
		}
		else{
			enableReflectors();
		}
	}
	
	public void disableSelectedReflectors(){
		for(int i=0;i<3;i++){
			if(reflectors.get(i).isSelected()){
				reflectors.get(i).setEnabled(false);
			}
		}
	}
	
	public void enableReflectors(){
		for(int i=0;i<3;i++){
			reflectors.get(i).setEnabled(true);
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("changeClosureNo")){
			changeClosureNo();
		}
		else if(e.getActionCommand().equals("reflector")){
			changeReflectors();
		}
	}
	
	/*public static void main(String[] args){
		java.awt.EventQueue.invokeLater(new Runnable(){
				public void run(){
					BombeSettings settings = new BombeSettings();
				}
			}
		);
	}*/
}