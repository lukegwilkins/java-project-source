package enigmaComponents;
import java.util.HashSet;
import java.lang.Character;
//import java.util.Arrays;

//abstract class for the rotors
public abstract class BasicRotor{
	//declares variables for the class
	
	//stores the position of the rotor by storing what input wire, wire 0 is connected to
	private int position;
	
	//variable to store the circuitry of the rotors
	private int[] circuitry;
	
	private Printer printer;
	
	private boolean printerEnabled;
	
	//method to set the position, position must be an int between 1 and 26 inclusive
	public void setPosition(int position){
		if(position<1 || position >26){
			throw new IllegalArgumentException("Position must be between 1 and 26 inclusive");
		}
		else{
			this.position = position;
		}
	}
	
	//alt method for setting position that uses chars
	public void setPosition(char positionChar){
		//we subtract 96 the ascii for 'a' -1 to get the position
		int position = (int) (Character.toLowerCase(positionChar))-96;
		//if the position is too small or too large we throw an error
		if(position<1 || position >26){
			throw new IllegalArgumentException("Position must be a character between A and Z inclusive");
		}
		else{
			this.position = position;
		}
	}
	
	//returns the current position
	public int getPosition(){
		return position;
	}
	
	//sets the circuitry
	public void setCircuitry(int[] circuitry){
		//the circuit must have length 26
		if(circuitry.length != 26){
			throw new IllegalArgumentException("The array must have length exactly 26");
		}
		else{
			//hashmap to store values in the circuit
			HashSet<Integer> circuitrySet = new HashSet<Integer>();
			int value;
			//we loop through and check all the values in the circuit
			for(int i=0; i<circuitry.length; i++){
				value = circuitry[i];
				//if the circuit has a value less than 0 or greater than 25, i.e. it encrypts to a letter outside the alphabet we throw an error
				if(value<0 || value>26){
					throw new IllegalArgumentException("The array must have values between 0 and 25 inclusive");
				}
				//we also throw an error if the circuit encrypts to wires to the same wire, i.e. it encrypts 2 letters to the same letter
				else if(!(circuitrySet.add(value))){
					throw new IllegalArgumentException("The array cannot have repeat values");
				}
			}
			this.circuitry = circuitry;
		}
	}
	
	//returns the circuitry array
	public int[] getCircuitry(){
		return circuitry;
	}
	
	//returns the circuit but replaces the values in the circuit to their corresponding letters
	public char[] getCharCircuitry(){
		char[] returnCircuit = new char[26];
		for(int i=0; i<26;i++){
			returnCircuit[i]=Character.toUpperCase((char)(97+circuitry[i]));
		}
		return returnCircuit;
	}
	
	//used to set the printer for printing
	public void setPrinter(Printer printer){
		this.printer = printer;
	}
	
	//gets the printer
	public Printer getPrinter(){
		return printer;
	}
	
	//enables the printer
	public void enablePrinter(){
		printerEnabled = true;
	}
	
	//disables the printer
	public void disablePrinter(){
		printerEnabled = false;
	}
	
	//returns if the printer is enabled or not
	public boolean printerEnabled(){
		return printerEnabled;
	}
	
	//abstract method for setting the circuitry
	abstract void setCircuitry(char[] circuitry);
	
	//abstract method for encrypt
	abstract char encrypt(char letter);
	
	//abstract method for rotate
	abstract void rotate();
	
}