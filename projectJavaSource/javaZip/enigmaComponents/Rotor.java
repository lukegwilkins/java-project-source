package enigmaComponents;
import java.util.HashSet;

import java.lang.Character;

public class Rotor extends BasicRotor{
	
	//declares variables for use in the class
	private int turnoverPosition;
	private int ringPosition;
	private int[] reverseCircuitry;
	private BasicRotor nextRotor;
	private char turnoverChar;
	private boolean doubleStepEnabled;
	
	//constructor for rotor
	public Rotor(){
		turnoverPosition=1;
		ringPosition=1;
		turnoverChar='a';
		setPosition(1);
		int[] initialCircuitry = new int[26];
		for(int i =0; i<26;i++){
			initialCircuitry[i]=i;
		}
		setCircuitry(initialCircuitry);
		//we set the next rotor to initially be a non rotating reflector
		nextRotor = new NonRotReflector();
		
		enablePrinter();
		
		doubleStepEnabled = false;
	}
	
	//setCircuitry method for the rotor
	@Override
	public void setCircuitry(int[] circuitry){
		if(circuitry.length != 26){
			throw new IllegalArgumentException("The array must have length exactly 26");
		}
		else{
			HashSet<Integer> circuitrySet = new HashSet<Integer>();
			int value;
			int[] reverseCircuitry = new int[26];
			for(int i=0; i<circuitry.length; i++){
				value = circuitry[i];
				if(value<0 || value>25){
					throw new IllegalArgumentException("The array must have values between 0 and 25 inclusive");
				}
				else if(!(circuitrySet.add(value))){
					throw new IllegalArgumentException("The array cannot have repeat values");
				}
				reverseCircuitry[value]=i;
			}
			super.setCircuitry(circuitry);
			this.reverseCircuitry = reverseCircuitry;
		}
	}
	
	//set circuitry
	public void setCircuitry(char[] circuitry){
		int[] intCircuit = new int[26];
		if(circuitry.length != 26){
			throw new IllegalArgumentException("The array must have length exactly 26");
		}
		else{
			for(int i=0; i<circuitry.length; i++){
				int ascii = (int) Character.toLowerCase(circuitry[i]);
				intCircuit[i]=ascii-97;
			}
			setCircuitry(intCircuit);
		}
	}
	
	//returns the reverseCircuitry array, primarily for testing
	public int[] getReverseCircuitry(){
		return reverseCircuitry;
	}
	
	//rotate method
	public void rotate(){
		int newPosition=(getPosition()+1)%27;
		
		if(newPosition==0){
			newPosition=1;
		}
		
		setPosition(newPosition);
		if(newPosition==turnoverPosition){
			nextRotor.rotate();
		}
		else if(nextRotor instanceof Rotor){
			Rotor temp = (Rotor) nextRotor;
			//System.out.println("char on top is " + temp.getCharOnTop() + " turnover char is " +temp.getTurnoverChar());
			if(temp.doubleStepEnabled() && (temp.getCharOnTop()==temp.getTurnoverChar())){
				nextRotor.rotate();
			}
		}
		
	}
	
	//the method to set the ring position
	public void setRingPosition(int ringPosition) throws IllegalArgumentException{
		//letter = Character.toLowerCase(letter);
		//int ascii = (int) letter;
		if( ringPosition<1 || ringPosition>26){
			throw new IllegalArgumentException("ring position must be an integer between 1 and 26 inclusive");
		}
		else{
			this.ringPosition=ringPosition;
			//setTurnoverChar(turnoverChar);
		}
		
	}
	
	//method for getting the ring position
	public int getRingPosition(){
		return ringPosition;
	}
	
	//figures out which character is currently been shown on the top of the rotor
	public char getCharOnTop(){
		return (char) (getPosition()+96);
	}
	
	//figures out the turnover position from the give char
	public void setTurnoverChar(char turnoverChar){
		int ascii = (int) Character.toLowerCase(turnoverChar);
		if( ascii<97 || ascii>122){
			throw new IllegalArgumentException("Character must be a letter between a-z inclusive");
		}
		else{
			turnoverPosition = (ascii-96 + 1)%27;
			if(turnoverPosition==0){
				turnoverPosition=1;
			}
			this.turnoverChar = Character.toLowerCase(turnoverChar);
		}
	}
	
	//returns turnoverPosition used for testing
	public int getTurnoverPosition(){
		return turnoverPosition;
	}
	
	//returns turnoverChar
	public char getTurnoverChar(){
		return turnoverChar;
	}
	
	//method for setting the nextRotor
	//bug fix so can't have itself as a rotor
	public void setNextRotor(BasicRotor nextRotor) throws IllegalArgumentException{
		if(this != nextRotor){
			this.nextRotor = nextRotor;
		}
		else{
			throw new IllegalArgumentException("A rotor cannot have itself as the next rotor");
		}
	}
	
	//returns the nextRotor
	public BasicRotor getNextRotor(){
		return nextRotor;
	}
	
	//encrypt method
	public char encrypt(char letter){
		int ascii = (char) Character.toLowerCase(letter);
		if(ascii <97 || ascii >122){
			throw new IllegalArgumentException("letter must be between a and z inclusive");
		}
		else{
			int shift = getPosition() - ringPosition;
			
			Printer printer = getPrinter();
			
			if(printerEnabled()){				
				printer.print("Shift is "+shift);
			}
			
			int wire = (26 + ascii-97 + shift)%26;
			
			if(printerEnabled()){
				printer.print("Wire is "+wire +" this is the letter "+(char)(97+wire));
			}
			
			int encryptedWire = getCircuitry()[wire];
			
			if(printerEnabled()){
				printer.print("Encrypted wire is "+encryptedWire+" this is the letter "+(char)(97+encryptedWire));
			}
			
			int outWire = (26 + encryptedWire - shift)%26;
			
			if(printerEnabled()){
				printer.print("Output wire is "+outWire+" this is the letter "+(char)(97+outWire));
			}
			
			if(printerEnabled()){
				printer.addTab();
			}
			
			char nextRotorEncrypt = nextRotor.encrypt((char)(97+outWire));
			
			if(printerEnabled()){
				printer.removeTab();
			}
			
			ascii = (char) Character.toLowerCase(nextRotorEncrypt);
			
			if(printerEnabled()){
				printer.print("Ascii is "+ascii);
			}
			
			wire = (26 + ascii-97 + shift)%26;
			
			if(printerEnabled()){
				printer.print("Wire is "+wire +" this is the letter "+(char)(97+wire));
			}
			
			encryptedWire = reverseCircuitry[wire];
			
			if(printerEnabled()){
				printer.print("Encrypted wire is "+encryptedWire+" this is the letter "+(char)(97+encryptedWire));
			}
			
			outWire = (26 + encryptedWire - shift)%26;
			
			if(printerEnabled()){
				printer.print("Output wire is "+outWire+" this is the letter "+(char)(97+outWire));
			}
			
			return (char) (outWire + 97);
		}

	}
	
	@Override
	public void setPrinter(Printer printer){
		super.setPrinter(printer);
		nextRotor.setPrinter(printer);
	}
	
	@Override
	public void enablePrinter(){
		super.enablePrinter();
		nextRotor.enablePrinter();
	}
	
	@Override
	public void disablePrinter(){
		super.disablePrinter();
		nextRotor.disablePrinter();
	}
	
	public void enableDoubleStep(){
		doubleStepEnabled = true;
	}
	
	public void disableDoubleStep(){
		doubleStepEnabled = false;
	}
	
	public boolean doubleStepEnabled(){
		return doubleStepEnabled;
	}
}