package enigmaComponents;
import java.util.HashSet;
import java.util.Arrays;
public abstract class Reflector extends BasicRotor{
	
	//encrypt method for the reflector, it figures out the actual wire powered based on position
	//encrypts it, then figures out which wire is powered and outputs it
	public char encrypt(char letter){
		int wire = (int) letter-97;
		if(wire <0 || wire >25){
			throw new IllegalArgumentException("wire must be between 0 and 25 inclusive");
		}
		else{
			int position = getPosition();			
			Printer printer = getPrinter();
			
			wire = (wire + position-1)%26;
			
			if(printerEnabled()){
				printer.print("The wire is "+ wire +" this is the letter "+(char)(97+wire));
			}
			
			int encryptedWire = getCircuitry()[wire];
			
			if(printerEnabled()){
				printer.print("The encrypted wire is "+ encryptedWire +" this is the letter "+(char)(97+encryptedWire));
			}
			
			int outWire = (26 + encryptedWire - position+1)%26;
			return (char) (outWire + 97);
		}
	}
	
	//method for setting the circuitry
	@Override
	public void setCircuitry(int[] circuitry) throws IllegalArgumentException{
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
				else if(i == value){
					throw new IllegalArgumentException("The Reflector unit can't encrypt a wire as itself");
				}
				else if(circuitry[value]!=i){
					throw new IllegalArgumentException("The Reflector must have involution, e.g. if 5 is connected to 10, 10 must be connected to 5");
				}
			}
			super.setCircuitry(circuitry);
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
	
}