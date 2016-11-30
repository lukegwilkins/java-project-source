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
		//sets turnoverPosition to be 1
		turnoverPosition=1;
		//sets ring position to be 1
		ringPosition=1;
		//sets turnoverChar to be a
		turnoverChar='a';
		//sets turn over position to be 1
		setPosition(1);
		
		//set initial circuity which encrypts a letter as itself
		int[] initialCircuitry = new int[26];
		for(int i =0; i<26;i++){
			initialCircuitry[i]=i;
		}
		setCircuitry(initialCircuitry);
		//we set the next rotor to initially be a non rotating reflector
		nextRotor = new NonRotReflector();
		
		//disables the printer by default
		disablePrinter();
		
		doubleStepEnabled = false;
	}
	
	//setCircuitry method for the rotor
	@Override
	public void setCircuitry(int[] circuitry){
		//checks that the array has a length exactly 26
		if(circuitry.length != 26){
			throw new IllegalArgumentException("The array must have length exactly 26");
		}
		else{
			//hashset used to check circuitry
			HashSet<Integer> circuitrySet = new HashSet<Integer>();
			int value;
			int[] reverseCircuitry = new int[26];
			for(int i=0; i<circuitry.length; i++){
				value = circuitry[i];
				//circuit has to have values between 0 and 25 i.e. a letter has to be encrypted as a letter in the alphabet
				if(value<0 || value>25){
					throw new IllegalArgumentException("The array must have values between 0 and 25 inclusive");
				}
				//2 letters cannot be encrypted as the same letter
				else if(!(circuitrySet.add(value))){
					throw new IllegalArgumentException("The array cannot have repeat values");
				}
				//we also set up the reverseCircuitry as we are checking the input circuit, the reverseCircuitry is the same
				//as circuit but the values are swapped
				reverseCircuitry[value]=i;
			}
			super.setCircuitry(circuitry);
			this.reverseCircuitry = reverseCircuitry;
		}
	}
	
	//set circuitry for a Char array
	public void setCircuitry(char[] circuitry){
		//converts the char array into the equivalent int array and sets the circuitry using the alternate setCircuitry method
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
		//gets the new position we mod this by 27 as we want values between 1 and 26
		int newPosition=(getPosition()+1)%27;
		
		//if we have reset back round to 0, then the new position is set to 1
		if(newPosition==0){
			newPosition=1;
		}
		
		//we set the position to be the new position
		setPosition(newPosition);
		//if a turnover position has occurred we rotate the next rotor
		if(newPosition==turnoverPosition){
			nextRotor.rotate();
		}
		else if(nextRotor instanceof Rotor){
			Rotor temp = (Rotor) nextRotor;
			//if the next rotor is an instance of Rotor, and it has double step enable, and its character
			//on top is the turnover character then we rotate it
			if(temp.doubleStepEnabled() && (temp.getCharOnTop()==temp.getTurnoverChar())){
				nextRotor.rotate();
			}
		}
		
	}
	
	//the method to set the ring position
	public void setRingPosition(int ringPosition) throws IllegalArgumentException{
		//if the ring position is our of range we throw an error
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
	
	//figures out the turnover position from the given char
	public void setTurnoverChar(char turnoverChar){
		int ascii = (int) Character.toLowerCase(turnoverChar);
		//if the char isn't in the alphabet we throw an error
		if( ascii<97 || ascii>122){
			throw new IllegalArgumentException("Character must be a letter between a-z inclusive");
		}
		else{
			//we get the turnover position from the character, the char doesn't say at which position the next rotor needs to rotate
			//but it says what the char on top will be when the next rotation will rotate the next rotor, so if the turnover char is a
			//when a is on top, then the next rotation will rotate the next rotor, if b is on top then that means that the next rotor
			//was just rotated, so if the turnover char is a, the turnover position is 2, as a is position 1
			turnoverPosition = (ascii-95)%27;
			
			//if the turnoverPosition is 0 we set it to 1
			if(turnoverPosition==0){
				turnoverPosition=1;
			}
			//we store the char
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
	public void setNextRotor(BasicRotor nextRotor) throws IllegalArgumentException{
		//if the next rotor is itself then throw an error
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
		int ascii = (int) Character.toLowerCase(letter);
		//if the letter isn't between a-z we throw an error
		if(ascii <97 || ascii >122){
			throw new IllegalArgumentException("letter must be between a and z inclusive");
		}
		else{
			//we get the shift from position and ring position
			int shift = getPosition() - ringPosition;
			
			//we get the printer
			Printer printer = getPrinter();
			
			//if the printer is enabled we print the shift
			if(printerEnabled()){				
				printer.print("Shift is "+shift);
			}
			
			//we get the wire powered due to the shift
			int wire = (26 + ascii-97 + shift)%26;
			
			//if the printer is enabled we print the wire and letter this corresponds to
			if(printerEnabled()){
				printer.print("Wire is "+wire +" this is the letter "+(char)(97+wire));
			}
			
			//we encrypted the wire
			int encryptedWire = getCircuitry()[wire];
			
			//if printer is enabled we print the encrypted wire and its letter
			if(printerEnabled()){
				printer.print("Encrypted wire is "+encryptedWire+" this is the letter "+(char)(97+encryptedWire));
			}
			
			//we get the output wire due to the shift
			int outWire = (26 + encryptedWire - shift)%26;
			
			//if the printer is enabled we print this and add a tab to the printer for the next rotor
			if(printerEnabled()){
				printer.print("Output wire is "+outWire+" this is the letter "+(char)(97+outWire));
				printer.addTab();
			}
			
			//we get the next rotor to encrypr
			char nextRotorEncrypt = nextRotor.encrypt((char)(97+outWire));
			
			//if the printer is enabled we remove the tab
			if(printerEnabled()){
				printer.removeTab();
			}
			
			//we get the ascii of the letter in lowercase
			ascii = (int) Character.toLowerCase(nextRotorEncrypt);
			
			//we print the ascii value
			if(printerEnabled()){
				printer.print("Ascii is "+ascii);
			}
			
			//we get the powered wire due to the shift
			wire = (26 + ascii-97 + shift)%26;
			
			//we print the wire and its corresponding letter
			if(printerEnabled()){
				printer.print("Wire is "+wire +" this is the letter "+(char)(97+wire));
			}
			
			//we encrypted the wire using the reverse circuit
			encryptedWire = reverseCircuitry[wire];
			
			//we print the encrypted wire and its corresponding letter
			if(printerEnabled()){
				printer.print("Encrypted wire is "+encryptedWire+" this is the letter "+(char)(97+encryptedWire));
			}
			
			//we get the output wire due to the shift
			outWire = (26 + encryptedWire - shift)%26;
			
			//we print the output wire
			if(printerEnabled()){
				printer.print("Output wire is "+outWire+" this is the letter "+(char)(97+outWire));
			}
			
			//we return the output wire
			return (char) (outWire + 97);
		}

	}
	
	//method for setting the printer
	@Override
	public void setPrinter(Printer printer){
		super.setPrinter(printer);
		//we set the next rotor to have the same printer
		nextRotor.setPrinter(printer);
	}
	
	//method for enabling the printer
	@Override
	public void enablePrinter(){
		super.enablePrinter();
		//enables the printer for the next rotor
		nextRotor.enablePrinter();
	}
	
	//method for disabling the printer
	@Override
	public void disablePrinter(){
		super.disablePrinter();
		//disables the printer for the next rotor
		nextRotor.disablePrinter();
	}
	
	//enables doublestep for the rotor
	public void enableDoubleStep(){
		doubleStepEnabled = true;
	}
	
	//disables doublestep for the rotor
	public void disableDoubleStep(){
		doubleStepEnabled = false;
	}
	
	//checks if doublestep is enabled
	public boolean doubleStepEnabled(){
		return doubleStepEnabled;
	}
}