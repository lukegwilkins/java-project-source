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
		int position = (int) (Character.toLowerCase(positionChar))-96;
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
		if(circuitry.length != 26){
			throw new IllegalArgumentException("The array must have length exactly 26");
		}
		else{
			HashSet<Integer> circuitrySet = new HashSet<Integer>();
			int value;
			for(int i=0; i<circuitry.length; i++){
				value = circuitry[i];
				if(value<0 || value>26){
					throw new IllegalArgumentException("The array must have values between 0 and 25 inclusive");
				}
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
	
	public char[] getCharCircuitry(){
		char[] returnCircuit = new char[26];
		for(int i=0; i<26;i++){
			returnCircuit[i]=Character.toUpperCase((char)(97+circuitry[i]));
		}
		return returnCircuit;
	}
	public void setPrinter(Printer printer){
		this.printer = printer;
	}
	
	public Printer getPrinter(){
		return printer;
	}
	
	public void enablePrinter(){
		printerEnabled = true;
	}
	
	public void disablePrinter(){
		printerEnabled = false;
	}
	
	public boolean printerEnabled(){
		return printerEnabled;
	}
	
	abstract void setCircuitry(char[] circuitry);
	
	//abstract method for encrypt
	abstract char encrypt(char letter);
	
	//abstract method for rotate
	abstract void rotate();
	
}