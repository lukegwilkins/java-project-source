package enigmaComponents;
import java.util.HashSet;
import java.util.Arrays;
public abstract class Reflector extends BasicRotor{
	
	//encrypt method for the reflector, it figures out the actual wire powered based on position
	//encrypts it, then figures out which wire is powered and outputs it
	public char encrypt(char letter){
		int wire = (int) letter-97;
		//if the input isn't in the alphabet through an error
		if(wire <0 || wire >25){
			throw new IllegalArgumentException("character must be between a and z inclusive");
		}
		else{
			//gets the position and the printer
			int position = getPosition();			
			Printer printer = getPrinter();
			
			//figures out the actual wire powered based on the position
			wire = (wire + position-1)%26;
			
			//if the printer is enabled we output what wire is powered
			if(printerEnabled()){
				printer.print("The wire is "+ wire +" this is the letter "+(char)(97+wire));
			}
			
			//this encrypts the wire
			int encryptedWire = getCircuitry()[wire];
			
			//prints the encryptedWire and what letter that corresponds to
			if(printerEnabled()){
				printer.print("The encrypted wire is "+ encryptedWire +" this is the letter "+(char)(97+encryptedWire));
			}
			
			//figures out the actual wire powered once the position is taken into account
			int outWire = (26 + encryptedWire - position+1)%26;
			//returns the encrypted character
			return (char) (outWire + 97);
		}
	}
	
	//method for setting the circuitry
	@Override
	public void setCircuitry(int[] circuitry) throws IllegalArgumentException{
		//circuit must be of length 26
		if(circuitry.length != 26){
			throw new IllegalArgumentException("The array must have length exactly 26");
		}
		else{
			//hashset used to make sure the array doesn't have repeat values
			HashSet<Integer> circuitrySet = new HashSet<Integer>();
			int value;
			for(int i=0; i<circuitry.length; i++){
				value = circuitry[i];
				//if a letter is encrypted as a value outside of the alphabet then an error is thrown
				if(value<0 || value>26){
					throw new IllegalArgumentException("The array must have values between 0 and 25 inclusive");
				}
				//2 letters can be encrypted as the same letter
				else if(!(circuitrySet.add(value))){
					throw new IllegalArgumentException("The array cannot have repeat values");
				}
				//for a reflector a wire cannot be encrypted as itself
				else if(i == value){
					throw new IllegalArgumentException("The Reflector unit can't encrypt a wire as itself");
				}
				//the reflector must have involution
				else if(circuitry[value]!=i){
					throw new IllegalArgumentException("The Reflector must have involution, e.g. if 5 is connected to 10, 10 must be connected to 5");
				}
			}
			//sets the circuitry using the setCircuitry method in BasicRotor
			super.setCircuitry(circuitry);
		}
	}
	
	//set circuitry for a char array
	public void setCircuitry(char[] circuitry){
		int[] intCircuit = new int[26];
		//checks the arrays size
		if(circuitry.length != 26){
			throw new IllegalArgumentException("The array must have length exactly 26");
		}
		else{
			//converts it to an array of int which is then passed to the other setCircuitry method
			for(int i=0; i<circuitry.length; i++){
				int ascii = (int) Character.toLowerCase(circuitry[i]);
				intCircuit[i]=ascii-97;
			}
			setCircuitry(intCircuit);
		}
	}
	
}